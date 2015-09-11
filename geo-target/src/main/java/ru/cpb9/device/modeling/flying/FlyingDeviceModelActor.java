package ru.cpb9.device.modeling.flying;

import akka.actor.ActorRef;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.device.modeling.KnownTmMessages;
import ru.cpb9.device.modeling.ModelActor;
import ru.cpb9.device.modeling.ModelTick;
import ru.cpb9.device.modeling.TmMessageFqn;
import ru.cpb9.geotarget.DeviceGuid;
import ru.cpb9.geotarget.akka.messages.TmMessage;
import ru.mipt.acsl.DeviceComponent;
import ru.mipt.acsl.MotionComponent;
import scala.concurrent.duration.FiniteDuration;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Artem Shein
 */
public class FlyingDeviceModelActor extends ModelActor
{

    private final FlyingDeviceModelExchangeController exchangeController;

    private LatLon latLon = new LatLon(Angle.fromDegreesLatitude(43.01770), Angle.fromDegreesLongitude(42.69012));
    private Angle pitch = Angle.fromDegrees(45.);
    private Angle heading = Angle.fromDegrees(-75.);
    private Angle roll = Angle.fromDegrees(-15.);
    private double altitude = 200.0;
    private byte throttle = 25;
    private short batteryLevel = 70;
    private short signalLevel = 100;
    private int crc = 0xFE9234A7;
    private short kind = 1;
    private double speed = 20.;
    private double accuracy = 500.;
    private Random random;

    public FlyingDeviceModelActor(@NotNull FlyingDeviceModelExchangeController exchangeController, @NotNull ActorRef tmServer)
    {
        super(tmServer);
        this.exchangeController = exchangeController;
        getContext().system().scheduler().schedule(FiniteDuration.Zero(), new FiniteDuration(50, TimeUnit.MILLISECONDS),
                self(), new ModelTick(), getContext().dispatcher(), self());
    }

    @Override
    protected void tick()
    {
        altitude += 1.;
        random = new Random();
        throttle += random.nextInt(10) - 5;
        if (random.nextInt(100) == 99)
        {
            batteryLevel--;
        }
        signalLevel = (short) random.nextInt(101);
        speed += random.nextInt(100) - 50;
        speed = limit(speed, 0, 1200);
        throttle = limit(throttle, (byte) 0, (byte) 100);
        sendTm();
    }

    private static double limit(double value, double min, double max)
    {
        return Math.min(Math.max(min, value), max);
    }

    private static byte limit(byte value, byte min, byte max)
    {
        return (byte) Math.min(Math.max(min, value), max);
    }

    private void sendTm()
    {
        DeviceGuid deviceGuid = exchangeController.getDeviceGuid().get();
        TmMessage<MotionComponent.AllMessage> message = new TmMessage<>(deviceGuid,
                KnownTmMessages.MOTION_ALL, new MotionComponent.AllMessage(latLon.latitude.degrees,
                latLon.longitude.degrees, altitude, accuracy, speed, pitch.degrees, heading.degrees, roll.degrees, throttle));
        tmServer.tell(message, getSelf());
        TmMessage<DeviceComponent.AllMessage> deviceMessage = new TmMessage<>(deviceGuid,
                KnownTmMessages.DEVICE_ALL, new DeviceComponent.AllMessage(batteryLevel, signalLevel, crc, kind));
        tmServer.tell(deviceMessage, getSelf());
    }
}
