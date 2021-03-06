package ru.mipt.acsl.device.modeling.flying;

import akka.actor.ActorRef;
import org.jetbrains.annotations.NotNull;
import ru.mipt.acsl.geotarget.ActorsRegistry;
import ru.mipt.acsl.geotarget.DeviceGuid;
import ru.mipt.acsl.geotarget.SimpleDeviceGuid;
import ru.mipt.acsl.geotarget.akka.ActorName;
import ru.mipt.acsl.geotarget.exchange.DeviceExchangeController;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class FlyingDeviceModelExchangeController implements DeviceExchangeController
{
    private static int uniqueId = 0;
    @NotNull
    private final ActorRef actorRef;
    @NotNull
    private DeviceGuid deviceGuid = SimpleDeviceGuid.newInstance("model" + uniqueId++);

    public FlyingDeviceModelExchangeController(@NotNull ActorRef tmServer)
    {
        this.actorRef = ActorsRegistry.getInstance().makeActor(FlyingDeviceModelActor.class,
                ActorName.FLYING_DEVICE_MODEL + "_" + getDeviceGuid().get().toString(), this, tmServer);
    }

    public FlyingDeviceModelExchangeController(@NotNull ActorRef tmServer, @NotNull Coordinates coordinates,
                                               @NotNull Route route)
    {
        this.actorRef = ActorsRegistry.getInstance().makeActor(FlyingDeviceModelActor.class,
                ActorName.FLYING_DEVICE_MODEL + "_" + getDeviceGuid().get().toString(), this, tmServer, coordinates,
                route);
    }

    public Optional<DeviceGuid> getDeviceGuid()
    {
        return Optional.of(deviceGuid);
    }

    @Override
    public boolean isConnected()
    {
        return true;
    }
}
