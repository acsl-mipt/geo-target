package ru.cpb9.geotarget.akka.messages;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceGuid;
import ru.mipt.acsl.decode.model.domain.message.DecodeMessage;

import java.io.Serializable;

/**
 * @author Artem Shein
 */
public class TmMessage<T> implements Serializable
{
    @NotNull
    private final DeviceGuid deviceGuid;
    @NotNull
    private final T values;

    @NotNull
    private final DecodeMessage message;

    public TmMessage(@NotNull DeviceGuid deviceGuid, @NotNull DecodeMessage message, @NotNull T values)
    {
        this.deviceGuid = deviceGuid;
        this.message = message;
        this.values = values;
    }

    @NotNull
    public DecodeMessage getMessage()
    {
        return message;
    }

    @NotNull
    public T getValues()
    {
        return values;
    }

    @NotNull
    public DeviceGuid getDeviceGuid()
    {
        return deviceGuid;
    }
}
