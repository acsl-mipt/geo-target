package ru.cpb9.geotarget.ui.controls;

import javafx.beans.property.ObjectProperty;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.LastTmMessageValuePropertyKeeper;
import ru.cpb9.geotarget.model.Device;
import ru.mipt.acsl.DeviceComponent;
import ru.mipt.acsl.MotionComponent;

/**
 * @author Artem Shein
 */
public class DeviceInfo extends LastTmMessageValuePropertyKeeper
{
    @NotNull
    private Device device;

    public DeviceInfo(@NotNull Device device)
    {
        this.device = device;
    }

    @NotNull
    public Device getDevice()
    {
        return device;
    }

    public void setMotion(@NotNull MotionComponent.AllMessage motion)
    {
        setTmMessageValue(MotionComponent.AllMessage.class, motion);
    }

    @NotNull
    public ObjectProperty<MotionComponent.AllMessage> getMotionProperty()
    {
        return getTmMessageProperty(MotionComponent.AllMessage.class);
    }

    public void setDevice(@NotNull DeviceComponent.AllMessage device)
    {
        setTmMessageValue(DeviceComponent.AllMessage.class, device);
    }

    @NotNull
    public ObjectProperty<DeviceComponent.AllMessage> getDeviceProperty()
    {
        return getTmMessageProperty(DeviceComponent.AllMessage.class);
    }
}
