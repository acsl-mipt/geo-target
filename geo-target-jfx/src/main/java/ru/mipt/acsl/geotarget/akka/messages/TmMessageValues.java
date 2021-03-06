package ru.mipt.acsl.geotarget.akka.messages;

import org.jetbrains.annotations.NotNull;
import ru.mipt.acsl.geotarget.DeviceGuid;
import ru.mipt.acsl.decode.model.component.message.TmMessage;

import java.io.Serializable;

/**
 * @author Artem Shein
 */
public class TmMessageValues<T> implements Serializable
{
    @NotNull
    private final DeviceGuid deviceGuid;
    @NotNull
    private final T values;
    @NotNull
    private final TmMessage message;

    public TmMessageValues(@NotNull DeviceGuid deviceGuid, @NotNull TmMessage message, @NotNull T values)
    {
        this.deviceGuid = deviceGuid;
        this.message = message;
        this.values = values;
    }

    @NotNull
    public TmMessage getMessage()
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
