package ru.cpb9.geotarget;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cpb9.geotarget.model.Device;

import java.util.Optional;

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
    public void setActiveDevice(@NotNull Optional<Device> activeDevice)
    {
        activeDeviceHolder.setValue(activeDevice);
    }

    @NotNull
    @Override
    public ObservableValue<Optional<Device>> getActiveDevice()
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
