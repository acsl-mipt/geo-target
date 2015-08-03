package ru.cpb9.geotarget.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import ru.cpb9.geotarget.TmStatus;
import ru.cpb9.geotarget.Trait;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @author Alexander Kuchuk
 * @author Artem Shein
 */

public class Device
{
    @NotNull
    private final String name;
    @NotNull
    private SimpleObjectProperty<Firmware> firmware = new SimpleObjectProperty<>();
    @NotNull
    private Optional<String> info = Optional.empty();

    @NotNull
    private ObservableList<PositionOrientation> devicePositions = FXCollections.observableArrayList();

    public Device(@NotNull String name)
    {
        this.name = name;
    }

    public Device(@NotNull String name, @NotNull String info)
    {
        this(name);
        this.info = Optional.of(info);
    }

    public void setFirmware(@NotNull Firmware firmware)
    {
        this.firmware.set(firmware);
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    @NotNull
    public Optional<String> getInfo()
    {
        return info;
    }

    @NotNull
    public SimpleObjectProperty<Firmware> getFirmwareProperty()
    {
        return firmware;
    }

    @Nullable
    public Firmware getFirmware()
    {
        return firmware.get();
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("name", name).add("info", info).add("firmware", firmware).toString();
    }

    public void setInfo(@NotNull String info)
    {
        this.info = Optional.of(info);
    }

    @NotNull
    public SimpleObjectProperty<Firmware> firmwareProperty()
    {
        return firmware;
    }

    @Nullable
    public TraitInfo getTraitOrNull(@NotNull Trait trait)
    {
        return getTraitOrNull(trait.getName());
    }

    @Nullable
    public TraitInfo getTraitOrNull(@NotNull String trait)
    {
        Firmware firmware = getFirmware();
        return firmware == null ? null : firmware.getTraitInfoList().stream()
                .filter(traitInfo -> traitInfo.getName().equals(trait)).findAny().orElse(null);
    }

    @Nullable
    public TmParameter getTraitStatusOrNull(@NotNull TmStatus tmStatus)
    {
        TraitInfo traitInfo = getTraitOrNull(tmStatus.getTrait());
        return traitInfo == null ? null : traitInfo.getStatusMap().get(tmStatus.getName());
    }

    @Nullable
    public Double getTraitStatusValueAsDoubleOrNull(@NotNull TmStatus tmStatus)
    {
        try
        {
            return getTraitStatusValueAsDoubleOrThrow(tmStatus);
        }
        catch (Exception ignored)
        {
            return null;
        }
    }

    @NotNull
    public ObservableList<PositionOrientation> getDevicePositions() {
        return devicePositions;
    }



    /**
     * @throws java.lang.NullPointerException
     * @param tmStatus
     * @return
     */
    public double getTraitStatusValueAsDoubleOrThrow(@NotNull TmStatus tmStatus)
    {
        String value = Preconditions.checkNotNull(Preconditions.checkNotNull(getTraitStatusOrNull(tmStatus)).getValue());
        return Double.parseDouble(value);
    }

    public void addListenerToStatuses(@NotNull ChangeListener<String> changeListener,
                                      @NotNull TmStatus... statuses)
    {
        //TODO
    }

    public void addListenerToStatuses(@NotNull ChangeListener<String> changeListener,
                                      @NotNull List<TmStatus> statuses)
    {
        //TODO
    }
}
