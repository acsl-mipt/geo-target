package ru.mipt.acsl.geotarget.akka.client;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import org.jetbrains.annotations.NotNull;
import ru.mipt.acsl.geotarget.DeviceGuid;
import ru.mipt.acsl.geotarget.akka.messages.TmMessageSubscribe;
import ru.mipt.acsl.geotarget.akka.messages.TmMessageUnsubscribe;
import ru.mipt.acsl.decode.model.component.message.TmMessage;

/**
 * @author Artem Shein
 */
public abstract class TmClientActor extends UntypedActor
{
    protected final ActorRef tmServer;

    public TmClientActor(@NotNull ActorRef tmServer)
    {
        this.tmServer = tmServer;
    }

    protected void subscribeForDeviceMessage(@NotNull DeviceGuid deviceGuid, @NotNull TmMessage message)
    {
        tmServer.tell(new TmMessageSubscribe(deviceGuid, message), getSelf());
    }

    protected void unsubscribeFromDeviceMessage(@NotNull DeviceGuid deviceGuid, @NotNull TmMessage message)
    {
        tmServer.tell(new TmMessageUnsubscribe(deviceGuid, message), getSelf());
    }
}
