package ru.mipt.acsl.geotarget;

import akka.actor.ActorRef;
import gov.nasa.worldwind.geom.Position;
import ru.mipt.acsl.device.modeling.flying.Orientation;
import ru.mipt.acsl.device.modeling.flying.PositionOrientation;
import ru.mipt.acsl.geotarget.akka.client.TmClientActor;
import ru.mipt.acsl.geotarget.akka.messages.AllMessagesSubscribe;
import ru.mipt.acsl.geotarget.akka.messages.TmMessageValues;
import ru.mipt.acsl.MotionComponent;
import ru.mipt.acsl.device.modeling.KnownTmMessages;

import javax.swing.*;

/**
 * @author Artem Shein
 */
public class PositionOrientationUpdateActor extends TmClientActor
{

    private final DeviceRegistry deviceRegistry;

    PositionOrientationUpdateActor(DeviceRegistry deviceRegistry, ActorRef tmServer)
    {
        super(tmServer);
        this.deviceRegistry = deviceRegistry;
    }

    @Override
    public void preStart()
    {
        tmServer.tell(AllMessagesSubscribe.INSTANCE, getSelf());
    }

    @Override
    public void onReceive(Object o)
    {
        if (o instanceof TmMessageValues)
        {
            TmMessageValues<?> message = (TmMessageValues<?>) o;
            if (message.getMessage().equals(KnownTmMessages.MotionAll))
            {
                MotionComponent.AllMessage a = (MotionComponent.AllMessage) message.getValues();
                deviceRegistry.getDevices().stream()
                        .filter(d -> d.getDeviceGuid().map(guid -> guid.equals(message.getDeviceGuid()))
                                .orElse(false))
                        .forEach(d ->
                                SwingUtilities.invokeLater(() -> d.getDevicePositions().add(new PositionOrientation(
                                        Position.fromDegrees(a.getLatitude(), a.getLongitude(), a.getAltitude()),
                                        Orientation.fromDegrees(a.getHeading(), a.getPitch(), a.getRoll())))));
            }
            else
            {
                unhandled(o);
            }
        }
    }
}
