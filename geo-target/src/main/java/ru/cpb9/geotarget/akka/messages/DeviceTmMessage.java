package ru.cpb9.geotarget.akka.messages;

import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceGuid;
import ru.mipt.acsl.decode.model.domain.DecodeMessage;

import java.io.Serializable;

/**
 * @author Artem Shein
 */
public class DeviceTmMessage implements Serializable
{
    @NotNull
    private final DecodeMessage message;

    @NotNull
    private final DeviceGuid deviceGuid;

    public DeviceTmMessage(@NotNull DeviceGuid deviceGuid, @NotNull DecodeMessage message)
    {
        this.deviceGuid = deviceGuid;
        this.message = message;
    }

    @NotNull
    public DeviceGuid getDeviceGuid()
    {
        return deviceGuid;
    }

    @NotNull
    public DecodeMessage getMessage()
    {
        return message;
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
