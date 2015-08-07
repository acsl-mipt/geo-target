package ru.cpb9.geotarget.exchange.mavlink;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * @author Artem Shein
 */
public class MavlinkPacket
{
    private static final Logger LOG = LoggerFactory.getLogger(MavlinkPacket.class);
    private static final short[] MESSAGE_CRC = {50, 124, 137, 0, 237, 217, 104, 119, 0, 0, 0, 89, 0, 0, 0, 0, 0, 0, 0, 0, 214, 159, 220, 168, 24, 23, 170, 144, 67, 115, 39, 246, 185, 104, 237, 244, 222, 212, 9, 254, 230, 28, 28, 132, 221, 232, 11, 153, 41, 39, 78, 0, 0, 0, 15, 3, 0, 0, 0, 0, 0, 153, 183, 51, 59, 118, 148, 21, 0, 243, 124, 0, 0, 38, 20, 158, 152, 143, 0, 0, 0, 106, 49, 22, 143, 140, 5, 150, 0, 231, 183, 63, 54, 0, 0, 0, 0, 0, 0, 0, 175, 102, 158, 208, 56, 93, 138, 108, 32, 185, 84, 34, 174, 124, 237, 4, 76, 128, 56, 116, 134, 237, 203, 250, 87, 203, 220, 25, 226, 46, 29, 223, 85, 6, 229, 203, 1, 195, 109, 168, 181, 0, 0, 0, 0, 0, 0, 154, 178, 200, 0, 108, 86, 95, 224, 0, 0, 0, 0, 0, 22, 0, 0, 0, 0, 0, 0, 0, 0, 0, 28, 249, 182, 0, 0, 0, 0, 0, 0, 0, 153, 16, 29, 162, 0, 0, 0, 0, 0, 0, 90, 95, 36, 0, 0, 88, 0, 0, 0, 0, 254, 0, 0, 0, 0, 87, 19, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 90, 0, 0, 0, 0, 0, 0, 8, 204, 49, 170, 44, 83, 46, 0};

    private final short dataSize;
    private final byte sequence;
    private final short deviceId;
    private final short componentId;
    private final short messageId;
    private final short checksum;
    private final byte[] data;

    public MavlinkPacket(ByteBuffer byteBuffer)
    {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte startingSequence = byteBuffer.get();
        Preconditions.checkState((byte) 0xfe == startingSequence, String.format("Starting sequence fail: %x", startingSequence));
        int checksumDataStartPosition = byteBuffer.position();
        dataSize = (short)(byteBuffer.get() & 0xff);
        sequence = byteBuffer.get();
        deviceId = (short)(byteBuffer.get() & 0xff);
        componentId = (short)(byteBuffer.get() & 0xff);
        messageId = (short)(byteBuffer.get() & 0xff);
        LOG.debug("Parsing message: {}", messageId);
        Preconditions.checkState(byteBuffer.remaining() > dataSize + 2);
        data = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.position() + dataSize);
        byteBuffer.position(byteBuffer.position() + dataSize);
        byte[] checksumBuf = new byte[byteBuffer.position() - checksumDataStartPosition + 1];
        System.arraycopy(byteBuffer.array(), checksumDataStartPosition, checksumBuf, 0, checksumBuf.length - 1);
        checksumBuf[checksumBuf.length - 1] = (byte)(MESSAGE_CRC[messageId] & 0xff);
        checksum = byteBuffer.getShort();
        int computedChecksum = X25Crc16.compute(checksumBuf);
        Preconditions.checkState(checksum == computedChecksum, String.format("Checksum fail: %x != %x", checksum, computedChecksum));
    }

    public short getMessageId()
    {
        return messageId;
    }
}
