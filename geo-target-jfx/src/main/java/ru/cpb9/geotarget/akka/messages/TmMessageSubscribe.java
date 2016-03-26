package ru.cpb9.geotarget.akka.messages;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceGuid;
import ru.mipt.acsl.decode.model.domain.pure.component.messages.TmMessage;

/**
 * @author Artem Shein
 */
public class TmMessageSubscribe extends DeviceTmMessage
{
    public TmMessageSubscribe(@NotNull DeviceGuid deviceGuid, @NotNull TmMessage message)
    {
        super(deviceGuid, message);
    }
}