package ru.mipt.acsl.geotarget.akka;

import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Kuchuk.
 */


public enum ActorName
{
    TM_SERVER("TmServer"),
    DEVICE_REGISTRY_UPDATER("DeviceRegistryUpdater"), POSITION_UPDATE_ACTOR("PositionUpdater"),
    LOCAL_DB_SERVER("LocalDbServer"), FLYING_DEVICE_MODEL("FlyingDeviceModel"), DEVICE_LIST_UPDATE("DeviceListUpdate");

    @NotNull
    private String name;

    ActorName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @Override
    @NotNull
    public String toString() {
        return getName();
    }
}


