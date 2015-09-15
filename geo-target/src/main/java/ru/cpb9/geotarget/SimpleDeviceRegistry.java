package ru.cpb9.geotarget;

import akka.actor.ActorRef;
import gov.nasa.worldwind.geom.Position;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.device.modeling.KnownTmMessages;
import ru.cpb9.device.modeling.flying.Orientation;
import ru.cpb9.device.modeling.flying.PositionOrientation;
import ru.cpb9.geotarget.akka.ActorName;
import ru.cpb9.geotarget.akka.client.TmClientActor;
import ru.cpb9.geotarget.akka.messages.TmMessage;
import ru.cpb9.geotarget.model.Device;
import ru.mipt.acsl.MotionComponent;

import javax.swing.*;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public final class SimpleDeviceRegistry implements DeviceRegistry
{
    @NotNull
    private ObservableList<Device> devices = FXCollections.observableArrayList();

    @NotNull
    private ObjectProperty<Optional<Device>> activeDeviceProperty = new SimpleObjectProperty<>(Optional.empty());

    @NotNull
    public static SimpleDeviceRegistry newInstance(@NotNull ActorRef tmServer)
    {
        return new SimpleDeviceRegistry(tmServer);
    }

    @Override
    public void setActiveDevice(@NotNull Optional<Device> activeDevice)
    {
        this.activeDeviceProperty.setValue(activeDevice);
    }

    @NotNull
    @Override
    public ObjectProperty<Optional<Device>> activeDeviceProperty()
    {
        return activeDeviceProperty;
    }

    @NotNull
    public ObservableList<Device> getDevices()
    {
        return devices;
    }

    private SimpleDeviceRegistry(@NotNull ActorRef tmServer)
    {
        ActorsRegistry.getInstance().makeActor(PositionOrientationUpdateActor.class,
                ActorName.POSITION_UPDATE_ACTOR.getName(), this, tmServer);
    }

    public static class PositionOrientationUpdateActor extends TmClientActor
    {
        @NotNull
        private final DeviceRegistry deviceRegistry;

        public PositionOrientationUpdateActor(@NotNull DeviceRegistry deviceRegistry, @NotNull ActorRef tmServer)
        {
            super(tmServer);
            this.deviceRegistry = deviceRegistry;
        }

        @Override
        public void preStart()
        {
            deviceRegistry.getDevices().stream().forEach(this::subscribeForDevice);
            deviceRegistry.getDevices().addListener((ListChangeListener<Device>) c -> {
                while (c.next())
                {
                    if (!(c.wasPermutated() || c.wasUpdated()))
                    {
                        c.getRemoved().stream().forEach(this::unsubscribeFromDevice);
                        c.getAddedSubList().stream().forEach(this::subscribeForDevice);
                    }
                }
            });
        }

        private void unsubscribeFromDevice(@NotNull Device device)
        {
            unsubscribeFromDeviceMessage(device.getDeviceGuid().orElseThrow(AssertionError::new),
                    KnownTmMessages.MOTION_ALL);
        }

        private void subscribeForDevice(@NotNull Device device)
        {
            subscribeForDeviceMessage(device.getDeviceGuid().orElseThrow(AssertionError::new),
                    KnownTmMessages.MOTION_ALL);
        }

        @Override
        public void onReceive(Object o) throws Exception
        {
            if (o instanceof TmMessage)
            {
                TmMessage<?> message = (TmMessage) o;
                if (message.getMessage().equals(KnownTmMessages.MOTION_ALL))
                {
                    MotionComponent.AllMessage a = (MotionComponent.AllMessage) message.getValues();
                    deviceRegistry.getDevices().stream()
                            .filter(d -> d.getDeviceGuid().get().equals(message.getDeviceGuid())).findAny()
                            .ifPresent(d ->
                                    SwingUtilities.invokeLater(() ->
                                            d.getDevicePositions().add(new PositionOrientation(
                                                    Position.fromDegrees(a.getLatitude(), a.getLongitude()),
                                                    Orientation
                                                            .fromDegrees(a.getHeading(), a.getPitch(), a.getRoll())))));
                }
                else
                {
                    throw new AssertionError();
                }
            }
            else
            {
                unhandled(o);
            }
        }
    }
}
