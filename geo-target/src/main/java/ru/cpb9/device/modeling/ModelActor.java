package ru.cpb9.device.modeling;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.ModelRegistry;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;

/**
 * @author Artem Shein
 */
public abstract class ModelActor extends UntypedActor
{
    @NotNull
    protected final ActorRef tmServer;
    @NotNull
    protected static final IfDevRegistry modelRegistry = ModelRegistry.getRegistry();

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
