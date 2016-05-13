package ru.mipt.acsl.geotarget;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Artem Shein
 */
public class ActorsRegistry
{
    private static final String ACTOR_SYSTEM_NAME = "GeoTarget";
    private static volatile ActorsRegistry INSTANCE;
    @NotNull
    private final Set<ActorRef> actors = new HashSet<>();
    @NotNull
    private final ActorSystem actorSystem;

    public ActorsRegistry(@NotNull ActorSystem actorSystem)
    {
        this.actorSystem = actorSystem;
    }

    @NotNull
    public static ActorsRegistry getInstance()
    {
        if (INSTANCE == null)
        {
            synchronized (ActorsRegistry.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new ActorsRegistry(ActorSystem.create(ACTOR_SYSTEM_NAME));
                }
            }
        }
        return INSTANCE;
    }

    @NotNull
    private ActorRef registerActorRef(@NotNull ActorRef actorRef)
    {
        actors.add(actorRef);
        return actorRef;
    }

    @NotNull
    public ActorRef makeActor(@NotNull Class<? extends UntypedActor> actorClass, @NotNull String actorName,
                              @NotNull Object... parameters)
    {
        return registerActorRef(actorSystem.actorOf(Props.create(actorClass, parameters), actorName));
    }

    public void shutdown()
    {
        actors.forEach(actorSystem::stop);
        actorSystem.shutdown();
    }
}
