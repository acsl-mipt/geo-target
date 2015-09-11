package ru.cpb9.geotarget.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceGuid;
import ru.cpb9.geotarget.exchange.DeviceExchangeController;

import java.util.Optional;

/**
 * @author Alexander Kuchuk
 * @author Artem Shein
 */

public class Device
{
    @NotNull
    private final DeviceExchangeController exchangeController;
    @NotNull
    private ObservableList<PositionOrientation> devicePositions = FXCollections.observableArrayList();

    public Device(@NotNull DeviceExchangeController exchangeController)
    {
        this.exchangeController = exchangeController;
    }

    @NotNull
    public Optional<DeviceGuid> getDeviceGuid()
    {
        return exchangeController.getDeviceGuid();
    }
}
