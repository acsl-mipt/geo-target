package ru.cpb9.geotarget;

import ru.cpb9.geotarget.model.Device;
import javafx.beans.value.ObservableValueBase;
import org.jetbrains.annotations.Nullable;

/**
 * @author Artem Shein
 */
public class ActiveDeviceHolder extends ObservableValueBase<Device>
{
    @Nullable
    private Device value;

    public void setValue(Device value)
    {
        if (this.value != value)
        {
            this.value = value;
            fireValueChangedEvent();
        }
    }

    @Override
    @Nullable
    public Device getValue()
    {
        return value;
    }
}
