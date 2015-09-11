package ru.cpb9.geotarget.akka.messages;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceGuid;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;

import java.io.Serializable;

/**
 * @author Artem Shein
 */
public class TmMessageSubscribe extends DeviceTmMessage
{
    public TmMessageSubscribe(@NotNull DeviceGuid deviceGuid, @NotNull IfDevMessage message)
    {
        super(deviceGuid, message);
    }
}
