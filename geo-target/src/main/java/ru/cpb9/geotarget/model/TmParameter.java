package ru.cpb9.geotarget.model;

import com.google.common.base.MoreObjects;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * @author Alexander Kuchuk
 */

public class TmParameter
{
    @NotNull
    private final SimpleObjectProperty<LocalDateTime> time = new SimpleObjectProperty<>();
    @NotNull
    private final SimpleStringProperty device = new SimpleStringProperty();
    @NotNull
    private final SimpleStringProperty trait = new SimpleStringProperty();
    @NotNull
    private final SimpleStringProperty status = new SimpleStringProperty();
    @NotNull
    private final SimpleStringProperty value = new SimpleStringProperty();
    @NotNull
    private final SimpleStringProperty unit = new SimpleStringProperty();
    @NotNull
    private final SimpleStringProperty info = new SimpleStringProperty();

    @Nullable
    public LocalDateTime getTime()
    {
        return time.get();
    }

    @Nullable
    public String getUnit() {
        return unit.get();
    }

    @NotNull
    public SimpleStringProperty unitProperty() {
        return unit;
    }

    @NotNull
    public SimpleObjectProperty<LocalDateTime> timeProperty()
    {
        return time;
    }

    @Nullable
    public String getDevice()
    {
        return device.get();
    }

    @NotNull
    public SimpleStringProperty deviceProperty()
    {
        return device;
    }

    @Nullable
    public String getTrait()
    {
        return trait.get();
    }

    @NotNull
    public SimpleStringProperty traitProperty()
    {
        return trait;
    }

    @Nullable
    public String getStatus()
    {
        return status.get();
    }

    @NotNull
    public SimpleStringProperty statusProperty()
    {
        return status;
    }

    @NotNull
    public SimpleStringProperty valueProperty()
    {
        return value;
    }

    @Nullable
    public String getValue()
    {
        return value.get();
    }

    @Override
    @NotNull
    public String toString()
    {
        return MoreObjects.toStringHelper(this).omitNullValues().add("time", time.get())
                .add("device", device.get()).add("trait", trait.get()).add("status",
                        status.get()).add("value", value.get()).add("unit", unit.get()).add("info", info.get())
                .toString();
    }

    public void setTime(@Nullable LocalDateTime time)
    {
        this.time.set(time);
    }

    public void setDevice(@Nullable String device)
    {
        this.device.set(device);
    }

    public void setTrait(@Nullable String trait)
    {
        this.trait.set(trait);
    }

    public void setStatus(@Nullable String status)
    {
        this.status.set(status);
    }

    public void setValue(@Nullable String value)
    {
        this.value.set(value);
    }

    public void setUnit(@Nullable String unit)
    {
        this.unit.set(unit);
    }

    public void setInfo(@Nullable String info)
    {
        this.info.set(info);
    }

    @Nullable
    public String getInfo()
    {
        return info.get();
    }
}