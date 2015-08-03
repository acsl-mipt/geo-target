package ru.cpb9.geotarget;

import ru.cpb9.geotarget.model.Device;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Artem Shein
 */
public interface DeviceRegistry
{
    void setActiveDevice(@Nullable Device activeDevice);
    @NotNull
    public ObservableValue<Device> getActiveDevice();
    @NotNull
    public ObservableList<Device> getDevices();
}
