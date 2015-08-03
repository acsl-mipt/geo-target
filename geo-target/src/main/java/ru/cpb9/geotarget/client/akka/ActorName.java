package ru.cpb9.geotarget.client.akka;

import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Kuchuk.
 */


public enum ActorName
{
    NANOMSG_PUB_SUB("NanomsgPubSub"), NANOMSG("Nanomsg"),
    DEVICE_REGISTRY_UPDATER("DeviceRegistryUpdater"), POSITION_UPDATE_ACTOR("PositionUpdater"),
    LOCAL_DB_SERVER("LocalDbServer");

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


