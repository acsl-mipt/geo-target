package ru.mipt.acsl.device.modeling;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public abstract class ModelActor extends UntypedActor
{
    @NotNull
    protected final ActorRef tmServer;


    public ModelActor(@NotNull ActorRef tmServer)
    {
        this.tmServer = tmServer;
    }

    @Override
    public void onReceive(Object o) throws Exception
    {
        if (o instanceof ModelTick)
        {
            tick();
        }
        else
            unhandled(o);
    }

    protected abstract void tick();
}
