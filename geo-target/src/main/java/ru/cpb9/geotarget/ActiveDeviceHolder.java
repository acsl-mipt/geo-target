package ru.cpb9.geotarget;

import ru.cpb9.geotarget.exchange.DeviceExchangeController;
import ru.cpb9.geotarget.model.Device;
import javafx.beans.value.ObservableValueBase;
import org.jetbrains.annotations.Nullable;

/**
 * @author Artem Shein
 */
public class ActiveDeviceHolder extends ObservableValueBase<DeviceExchangeController>
{
    @Nullable
    private DeviceExchangeController value;

    public void setValue(DeviceExchangeController value)
    {
        if (this.value != value)
        {
            this.value = value;
            fireValueChangedEvent();
        }
    }

    @Override
    @Nullable
    public DeviceExchangeController getValue()
    {
        return value;
    }
}
