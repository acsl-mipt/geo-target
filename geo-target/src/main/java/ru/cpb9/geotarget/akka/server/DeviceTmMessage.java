package ru.cpb9.geotarget.akka.server;

import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceGuid;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;

/**
 * @author Artem Shein
 */
public class DeviceTmMessage
{
    @NotNull
    private final DeviceGuid deviceGuid;
    @NotNull
    private final IfDevMessage message;

    public DeviceTmMessage(@NotNull DeviceGuid deviceGuid, @NotNull IfDevMessage message)
    {
        this.deviceGuid = deviceGuid;
        this.message = message;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof DeviceTmMessage))
        {
            return false;
        }
        DeviceTmMessage deviceTmMessage = (DeviceTmMessage)o;
        return deviceGuid.equals(deviceTmMessage.deviceGuid) && message.equals(deviceTmMessage.message);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(deviceGuid, message);
    }
}
