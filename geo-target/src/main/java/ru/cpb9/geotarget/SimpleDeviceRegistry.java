package ru.cpb9.geotarget;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.model.Device;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public final class SimpleDeviceRegistry implements DeviceRegistry
{
    @NotNull
    private ObservableList<Device> devices = FXCollections.observableArrayList();

    @NotNull
    private ObjectProperty<Optional<Device>> activeDeviceProperty = new SimpleObjectProperty<>(Optional.empty());

    @NotNull
    public static SimpleDeviceRegistry newInstance()
    {
        return new SimpleDeviceRegistry();
    }

    @Override
    public void setActiveDevice(@NotNull Optional<Device> activeDevice)
    {
        this.activeDeviceProperty.setValue(activeDevice);
    }

    @NotNull
    @Override
    public ObjectProperty<Optional<Device>> activeDeviceProperty()
    {
        return activeDeviceProperty;
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
