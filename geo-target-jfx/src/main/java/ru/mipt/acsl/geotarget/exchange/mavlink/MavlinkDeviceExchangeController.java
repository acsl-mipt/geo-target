package ru.mipt.acsl.geotarget.exchange.mavlink;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mipt.acsl.geotarget.DeviceGuid;
import ru.mipt.acsl.geotarget.SimpleDeviceGuid;
import ru.mipt.acsl.geotarget.exchange.DeviceExchangeController;
import ru.mipt.acsl.geotarget.exchange.ExchangeError;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class MavlinkDeviceExchangeController implements DeviceExchangeController
{
    private static final Logger LOG = LoggerFactory.getLogger(MavlinkDeviceExchangeController.class);
    private static int uniqueId = 0;
    private final DeviceGuid deviceGuid = SimpleDeviceGuid.newInstance("mavlink" + uniqueId++);
    private volatile boolean isConnected = false;

    public static MavlinkDeviceExchangeController newInstance(int localPort)
    {
        return new MavlinkDeviceExchangeController(localPort);
    }

    private MavlinkDeviceExchangeController(int localPort)
    {
        new Thread(() -> {
            try
            {
                DatagramSocket readSocket = new DatagramSocket(localPort);
                byte[] buf = new byte[65535];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                readSocket.receive(packet);
                readSocket.close();
                try(DatagramChannel channel = DatagramChannel.open())
                {
                    channel.socket().bind(new InetSocketAddress(localPort));
                    channel.connect(new InetSocketAddress(packet.getAddress(), packet.getPort()));
                    isConnected = true;
                    Preconditions.checkState(channel.isBlocking());
                    ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
                    while (true)
                    {
                        byteBuffer.clear();
                        channel.receive(byteBuffer);
                        byteBuffer.rewind();
                        MavlinkPacket mavlinkPacket = new MavlinkPacket(byteBuffer);
                        LOG.debug("Received message: {}", mavlinkPacket.getMessageId());
                        if (Thread.interrupted())
                        {
                            break;
                        }
                    }
                }
            }
            catch (IOException e)
            {
                throw new ExchangeError(e);
            }
        }).start();
    }

    @Override
    public Optional<DeviceGuid> getDeviceGuid()
    {
        return Optional.of(deviceGuid);
    }

    @Override
    public boolean isConnected()
    {
        return isConnected;
    }

}
