package ru.cpb9.geotarget.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Kuchuk.
 */


public class TraitInfo
{
    @NotNull
    private final String name;
    @NotNull
    private final String kind;
    @NotNull
    private final String info;
    @NotNull
    private final ObservableMap<String, TmParameter> statusMap = FXCollections.observableHashMap();

    public TraitInfo(@NotNull String name)
    {
        this(name, "", "");
    }

    public TraitInfo(@NotNull String name, @NotNull String kind, @NotNull String info)
    {
        this.name = name;
        this.kind = kind;
        this.info = info;
    }

    @NotNull
    public String getInfo() {
        return info;
    }

    @NotNull
    public String getKind() {
        return kind;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public ObservableMap<String, TmParameter> getStatusMap()
    {
        return statusMap;
    }
}
