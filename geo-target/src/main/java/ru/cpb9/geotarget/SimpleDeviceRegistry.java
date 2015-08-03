package ru.cpb9.geotarget;

import ru.cpb9.geotarget.model.Device;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Artem Shein
 */
public final class SimpleDeviceRegistry implements DeviceRegistry
{
    private static final Logger LOG = LoggerFactory.getLogger(SimpleDeviceRegistry.class);

    @NotNull
    private ObservableList<Device> devices = FXCollections.observableArrayList();

    @NotNull
    private ActiveDeviceHolder activeDeviceHolder = new ActiveDeviceHolder();

    @NotNull
    public static SimpleDeviceRegistry newInstance()
    {
        return new SimpleDeviceRegistry();
    }

    @Override
    public void setActiveDevice(@Nullable Device activeDevice)
    {
        activeDeviceHolder.setValue(activeDevice);
    }

    @NotNull
    @Override
    public ObservableValue<Device> getActiveDevice()
    {
        return activeDeviceHolder;
    }

    @NotNull
    public ObservableList<Device> getDevices()
    {
        return devices;
    }

    private SimpleDeviceRegistry()
    {
    }
}
