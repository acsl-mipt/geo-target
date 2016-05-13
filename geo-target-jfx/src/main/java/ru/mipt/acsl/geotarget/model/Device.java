package ru.mipt.acsl.geotarget.model;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import ru.mipt.acsl.device.modeling.flying.PositionOrientation;
import ru.mipt.acsl.geotarget.DeviceGuid;
import ru.mipt.acsl.geotarget.LastTmMessageValuePropertyKeeper;
import ru.mipt.acsl.geotarget.exchange.DeviceExchangeController;
import ru.mipt.acsl.DeviceComponent;
import ru.mipt.acsl.MotionComponent;

import java.util.Optional;

/**
 * @author Alexander Kuchuk
 * @author Artem Shein
 */

public class Device extends LastTmMessageValuePropertyKeeper
{
    @NotNull
    private final DeviceExchangeController exchangeController;

    private ObservableList<PositionOrientation> devicePositions = FXCollections.observableArrayList();

    public Device(@NotNull DeviceExchangeController exchangeController)
    {
        this.exchangeController = exchangeController;
    }

    @NotNull
    public ObservableList<PositionOrientation> getDevicePositions()
    {
        return devicePositions;
    }

    @NotNull
    public Optional<DeviceGuid> getDeviceGuid()
    {
        return exchangeController.getDeviceGuid();
    }

    public void setMotion(@NotNull MotionComponent.AllMessage motion)
    {
        setTmMessageValue(MotionComponent.AllMessage.class, motion);
    }

    @NotNull
    public ObjectProperty<MotionComponent.AllMessage> getMotionProperty()
    {
        return getTmMessageProperty(MotionComponent.AllMessage.class);
    }

    public void setDevice(@NotNull DeviceComponent.AllMessage device)
    {
        setTmMessageValue(DeviceComponent.AllMessage.class, device);
    }

    @NotNull
    public ObjectProperty<DeviceComponent.AllMessage> getDeviceProperty()
    {
        return getTmMessageProperty(DeviceComponent.AllMessage.class);
    }
}
