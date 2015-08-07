package ru.cpb9.geotarget.akka.messages;

import ru.cpb9.geotarget.DeviceGuid;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;

import java.io.Serializable;

/**
 * @author Artem Shein
 */
public class TmMessageSubscribe implements Serializable
{
    private DeviceGuid deviceGuid;
    private IfDevMessage message;

    public DeviceGuid getDeviceGuid()
    {
        return deviceGuid;
    }

    public IfDevMessage getMessage()
    {
        return message;
    }
}
