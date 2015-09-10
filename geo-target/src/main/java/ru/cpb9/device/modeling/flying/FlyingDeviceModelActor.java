package ru.cpb9.device.modeling.flying;

import akka.actor.ActorRef;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.device.modeling.ModelActor;
import ru.cpb9.device.modeling.ModelTick;
import ru.cpb9.device.modeling.TmMessageFqn;
import ru.cpb9.geotarget.akka.messages.TmMessage;
import ru.mipt.acsl.MotionComponent;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

/**
 * @author Artem Shein
 */
public class FlyingDeviceModelActor extends ModelActor
{

    private final FlyingDeviceModelExchangeController exchangeController;

    private LatLon latLon = new LatLon(Angle.fromDegreesLatitude(43.01770), Angle.fromDegreesLongitude(42.69012));
    private Angle pitch = Angle.fromDegrees(45.);
    private Angle heading = Angle.fromDegrees(-75.);
    private Angle roll = Angle.fromDegrees(-15.);
    private double altitude = 200.0;
    private byte throttle = 25;

    public FlyingDeviceModelActor(@NotNull FlyingDeviceModelExchangeController exchangeController, @NotNull ActorRef tmServer)
    {
        super(tmServer);
        this.exchangeController = exchangeController;
        getContext().system().scheduler().schedule(FiniteDuration.Zero(), new FiniteDuration(50, TimeUnit.MILLISECONDS),
                self(), new ModelTick(), getContext().dispatcher(), self());
    }

    @Override
    protected void tick()
    {
        TmMessage<MotionComponent.AllMessage> message = new TmMessage<>(exchangeController.getDeviceGuid().get(),
                modelRegistry.getMessage(TmMessageFqn.MOTION_ALL.toString()).orElseThrow(AssertionError::new),
                new MotionComponent.AllMessage(latLon.latitude.degrees, latLon.longitude.degrees, altitude, 100., 20.,
                        pitch.degrees, heading.degrees, roll.degrees, throttle));
        tmServer.tell(message, getSelf());
    }
}
