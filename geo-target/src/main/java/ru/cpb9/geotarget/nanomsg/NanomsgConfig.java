package ru.cpb9.geotarget.nanomsg;

/**
 * @author Artem Shein
 */
public final class NanomsgConfig
{
    public static final String PROTOCOL_STRING = "tcp://";
    public static final int HANDSHAKE_PORT = 55555;
    public static final int SEND_TIMEOUT_MS = 1000;
    public static final int RECIVE_TIMEOUT_MS = 1000;

    private NanomsgConfig()
    {

    }
}
