package ru.cpb9.geotarget;

import ru.cpb9.geotarget.exchange.DeviceExchangeController;
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
    private ObservableList<DeviceExchangeController> devices = FXCollections.observableArrayList();

    @NotNull
    private ActiveDeviceHolder activeDeviceHolder = new ActiveDeviceHolder();

    @NotNull
    public static SimpleDeviceRegistry newInstance()
    {
        return new SimpleDeviceRegistry();
    }

    @Override
    public void setActiveDevice(@Nullable DeviceExchangeController activeDevice)
    {
        activeDeviceHolder.setValue(activeDevice);
    }

    @NotNull
    @Override
    public ObservableValue<DeviceExchangeController> getActiveDevice()
    {
        return activeDeviceHolder;
    }

    @NotNull
    public ObservableList<DeviceExchangeController> getDevices()
    {
        return devices;
    }

    private SimpleDeviceRegistry()
    {
    }
}
