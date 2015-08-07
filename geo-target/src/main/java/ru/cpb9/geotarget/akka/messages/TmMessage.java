package ru.cpb9.geotarget.akka.messages;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceGuid;
import ru.cpb9.ifdev.model.domain.IfDevComponent;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;

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
    private final IfDevMessage message;

    public TmMessage(@NotNull DeviceGuid deviceGuid, @NotNull IfDevMessage message, @NotNull T values)
    {
        this.deviceGuid = deviceGuid;
        this.message = message;
        this.values = values;
    }

    @NotNull
    public IfDevMessage getMessage()
    {
        return message;
    }

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
