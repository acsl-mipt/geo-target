package ru.cpb9.device.modeling.flying;

import akka.actor.ActorRef;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class FlyingModel1 extends FlyingDeviceModelActor
{
    public FlyingModel1(
            @NotNull FlyingDeviceModelExchangeController exchangeController, @NotNull ActorRef tmServer)
    {
        super(exchangeController, tmServer);
    }
}
