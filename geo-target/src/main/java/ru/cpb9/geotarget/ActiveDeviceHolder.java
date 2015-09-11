package ru.cpb9.geotarget;

import javafx.beans.value.ObservableValueBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cpb9.geotarget.model.Device;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ActiveDeviceHolder extends ObservableValueBase<Optional<Device>>
{
    @NotNull
    private Optional<Device> value = Optional.empty();

    public void setValue(@NotNull Optional<Device> value)
    {
        if (!this.value.equals(value))
        {
            this.value = value;
            fireValueChangedEvent();
        }
    }

    @Override
    @NotNull
    public Optional<Device> getValue()
    {
        return value;
    }
}
