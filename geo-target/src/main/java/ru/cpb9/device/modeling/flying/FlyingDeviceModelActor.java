package ru.cpb9.device.modeling.flying;

import akka.actor.ActorRef;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.device.modeling.ModelActor;
import ru.cpb9.geotarget.DeviceGuid;
import ru.cpb9.geotarget.SimpleDeviceGuid;
import ru.cpb9.geotarget.akka.messages.TmMessage;
import ru.cpb9.geotarget.exchange.DeviceExchangeController;
import ru.mipt.acsl.Motion;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class FlyingDeviceModelActor extends ModelActor implements DeviceExchangeController
{
    private static int uniqueId = 0;
    private int id = uniqueId++;
    private DeviceGuid deviceGuid = SimpleDeviceGuid.newInstance("model" + id);

    private LatLon latLon = new LatLon(Angle.fromDegreesLatitude(43.01770), Angle.fromDegreesLongitude(42.69012));
    private Angle pitch = Angle.fromDegrees(45.);
    private Angle heading = Angle.fromDegrees(-75.);
    private Angle roll = Angle.fromDegrees(-15.);
    private double altitude = 200.0;
    private byte throttle = 25;

    public FlyingDeviceModelActor(@NotNull ActorRef tmServer)
    {
        super(tmServer);
    }

    @Override
    public Optional<DeviceGuid> getDeviceGuid()
    {
        return Optional.of(deviceGuid);
    }

    @Override
    public boolean isConnected()
    {
        return true;
    }

    @Override
    protected void tick()
    {
        TmMessage message = new TmMessage(modelRegistry.getMessage("ru.mipt.acsl.Motion.all").orElseThrow(AssertionError::new),
                new Motion.AllMessage(latLon.latitude.degrees, latLon.longitude.degrees, altitude, 100., 20.,
                        pitch.degrees, heading.degrees, roll.degrees, throttle));
        tmServer.tell(message, getSelf());
    }
}
