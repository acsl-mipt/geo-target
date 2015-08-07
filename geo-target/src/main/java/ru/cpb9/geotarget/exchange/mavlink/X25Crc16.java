package ru.cpb9.geotarget.exchange.mavlink;

/**
 * @author Artem Shein
 */
public class X25Crc16
{
    public static int compute(byte[] buffer)
    {
        int result = 0xffff;
        for (byte data : buffer)
        {
            int tmp, tmpdata;
            int crcaccum = result & 0x000000ff;
            tmpdata = data & 0x000000ff;
            tmp = tmpdata ^ crcaccum;
            tmp &= 0x000000ff;
            int tmp4 = tmp << 4;
            tmp4 &= 0x000000ff;
            tmp ^= tmp4;
            tmp &= 0x000000ff;
            int crch = result >> 8;
            crch &= 0x0000ffff;
            int tmp8 = tmp << 8;
            tmp8 &= 0x0000ffff;
            int tmp3 = tmp << 3;
            tmp3 &= 0x0000ffff;
            tmp4 = tmp >> 4;
            tmp4 &= 0x0000ffff;
            int tmpa = crch ^ tmp8;
            tmpa &= 0x0000ffff;
            int tmpb = tmp3 ^ tmp4;
            tmpb &= 0x0000ffff;
            result = tmpa ^ tmpb;
            result &= 0x0000ffff;
        }
        return result;
    }
}
