package ru.cpb9.geotarget.akka.client;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.geotarget.DeviceGuid;
import ru.cpb9.geotarget.akka.messages.TmMessageSubscribe;
import ru.cpb9.geotarget.akka.messages.TmMessageUnsubscribe;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;

/**
 * @author Artem Shein
 */
public abstract class TmClientActor extends UntypedActor
{
    private final ActorRef tmServer;

    public TmClientActor(@NotNull ActorRef tmServer)
    {
        this.tmServer = tmServer;
    }

    protected void subscribeForDeviceMessage(@NotNull DeviceGuid deviceGuid, @NotNull IfDevMessage message)
    {
        tmServer.tell(new TmMessageSubscribe(deviceGuid, message), getSelf());
    }

    protected void unsubscribeFromDeviceMessage(@NotNull DeviceGuid deviceGuid, @NotNull IfDevMessage message)
    {
        tmServer.tell(new TmMessageUnsubscribe(deviceGuid, message), getSelf());
    }
}
