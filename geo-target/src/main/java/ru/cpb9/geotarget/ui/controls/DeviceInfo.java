package ru.cpb9.geotarget.ui.controls;

import javafx.beans.property.SimpleObjectProperty;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.model.Device;
import ru.mipt.acsl.DeviceComponent;
import ru.mipt.acsl.MotionComponent;

/**
 * @author Artem Shein
 */
public class DeviceInfo
{
    @NotNull
    private Device device;

    private SimpleObjectProperty<MotionComponent.AllMessage> motionProperty = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<DeviceComponent.AllMessage> deviceProperty = new SimpleObjectProperty<>(null);

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
        motionProperty.set(motion);
    }

    @NotNull
    public SimpleObjectProperty<MotionComponent.AllMessage> getMotionProperty()
    {
        return motionProperty;
    }

    public void setDevice(@NotNull DeviceComponent.AllMessage device)
    {
        deviceProperty.set(device);
    }

    @NotNull
    public SimpleObjectProperty<DeviceComponent.AllMessage> getDeviceProperty()
    {
        return deviceProperty;
    }
}
