package ru.mipt.acsl.geotarget;

import akka.actor.ActorRef;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import ru.mipt.acsl.geotarget.model.Device;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface DeviceRegistry {

    ObjectProperty<Device> activeDeviceProperty();

    ObservableList<Device> getDevices();

    default void setActiveDevice(Device activeDevice) {
        activeDeviceProperty().set(activeDevice);
    }

    default Optional<Device> getActiveDevice() {
        return Optional.ofNullable(activeDeviceProperty().get());
    }

    static DeviceRegistry newInstance(ActorRef tmServer) {
        return new DeviceRegistryImpl(tmServer);
    }

}