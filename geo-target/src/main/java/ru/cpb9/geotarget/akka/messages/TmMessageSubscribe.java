package ru.cpb9.geotarget.akka.messages;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceGuid;
import ru.mipt.acsl.decode.model.domain.Message;

/**
 * @author Artem Shein
 */
public class TmMessageSubscribe extends DeviceTmMessage
{
    public TmMessageSubscribe(@NotNull DeviceGuid deviceGuid, @NotNull Message message)
    {
        super(deviceGuid, message);
    }
}
