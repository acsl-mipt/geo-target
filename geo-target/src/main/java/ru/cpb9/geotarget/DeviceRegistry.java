package ru.cpb9.geotarget;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cpb9.geotarget.exchange.DeviceExchangeController;

/**
 * @author Artem Shein
 */
public interface DeviceRegistry
{
    void setActiveDevice(@Nullable DeviceExchangeController activeDevice);
    @NotNull
    public ObservableValue<DeviceExchangeController> getActiveDevice();
    @NotNull
    public ObservableList<DeviceExchangeController> getDevices();
}
