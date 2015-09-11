package ru.cpb9.geotarget;

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
    void setActiveDevice(@NotNull Optional<Device> activeDevice);
    @NotNull
    ObservableValue<Optional<Device>> getActiveDevice();
    @NotNull
    ObservableList<Device> getDevices();
}
