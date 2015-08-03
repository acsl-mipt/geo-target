package ru.cpb9.geotarget.model;

import com.google.common.base.MoreObjects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Kuchuk.
 */


public class Firmware
{
    @NotNull
    private final String name;
    @NotNull
    private final String info;
    @NotNull
    private final ObservableList<TraitInfo> traitInfoList = FXCollections.observableArrayList();

    public Firmware(@NotNull String name, @NotNull String info)
    {
        this.name = name;
        this.info = info;
    }

    @NotNull
    public String getName()
    {
        return name;
    }
    @NotNull
    public String getInfo()
    {
        return info;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("name", name).add("info", info).toString();
    }

    @NotNull
    public ObservableList<TraitInfo> getTraitInfoList()
    {
        return traitInfoList;
    }
}
