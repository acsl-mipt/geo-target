package ru.cpb9.geotarget.nanomsg;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public enum NanomsgServiceName
{
    CORE_ROUTER("mcc.core.router"), GEO_TARGET_GUI("mcc.java"), CORE_MANAGER("mcc.core.manager"), LOCAL_DB_SERVER("geotarget.db");

    @NotNull
    private final String name;

    NanomsgServiceName(@NotNull String name)
    {
        this.name = name;
    }

    @NotNull
    public String getName()
    {
        return name;
    }
}
