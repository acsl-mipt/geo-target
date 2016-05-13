package ru.mipt.acsl.geotarget.akka.messages;

import org.jetbrains.annotations.NotNull;
import ru.mipt.acsl.geotarget.DeviceGuid;
import ru.mipt.acsl.decode.model.component.message.TmMessage;

/**
 * @author Artem Shein
 */
public class TmMessageUnsubscribe extends DeviceTmMessage
{
    public TmMessageUnsubscribe(@NotNull DeviceGuid deviceGuid, @NotNull TmMessage message)
    {
        super(deviceGuid, message);
    }
}
