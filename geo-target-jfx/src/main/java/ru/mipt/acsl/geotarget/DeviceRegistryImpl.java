package ru.mipt.acsl.geotarget;

import akka.actor.ActorRef;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.mipt.acsl.geotarget.akka.ActorName;
import ru.mipt.acsl.geotarget.model.Device;

/**
 * @author Artem Shein
 */
class DeviceRegistryImpl implements DeviceRegistry
{

    private final ActorRef tmServer;
    private final ObservableList<Device> devices = FXCollections.observableArrayList();
    private final ObjectProperty<Device> activeDeviceProperty = new SimpleObjectProperty<>();

    DeviceRegistryImpl(ActorRef tmServer)
    {
        this.tmServer = tmServer;
        ActorsRegistry.getInstance().makeActor(PositionOrientationUpdateActor.class,
                ActorName.POSITION_UPDATE_ACTOR.getName(), this, tmServer);
    }

    @Override
    public ObservableList<Device> getDevices()
    {
        return devices;
    }

    @Override
    public ObjectProperty<Device> activeDeviceProperty()
    {
        return activeDeviceProperty;
    }

}
