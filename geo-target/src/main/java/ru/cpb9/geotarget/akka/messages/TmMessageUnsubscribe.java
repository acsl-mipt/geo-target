package ru.cpb9.geotarget.akka.messages;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceGuid;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;

/**
 * @author Artem Shein
 */
public class TmMessageUnsubscribe extends DeviceTmMessage
{
    public TmMessageUnsubscribe(@NotNull DeviceGuid deviceGuid, @NotNull IfDevMessage message)
    {
        super(deviceGuid, message);
    }
}
