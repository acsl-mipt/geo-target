package ru.cpb9.geotarget;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cpb9.geotarget.exchange.DeviceExchangeController;
import ru.cpb9.geotarget.model.Device;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface DeviceRegistry
{
    @NotNull
    ObjectProperty<Optional<Device>> activeDeviceProperty();

    @NotNull
    ObservableList<Device> getDevices();

    default void setActiveDevice(@NotNull Optional<Device> activeDevice)
    {
        activeDeviceProperty().set(activeDevice);
    }

    default Optional<Device> getActiveDevice()
    {
        return activeDeviceProperty().get();
    }
}
