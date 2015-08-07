package ru.cpb9.geotarget.akka.server;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.device.modeling.TmMessageFqn;
import ru.cpb9.geotarget.ModelRegistry;
import ru.cpb9.geotarget.akka.messages.TmMessage;
import ru.cpb9.geotarget.akka.messages.TmMessageSubscribe;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;

/**
 * @author Artem Shein
 */
public class TmServerActor extends UntypedActor
{
    private final Multimap<DeviceTmMessage, ActorRef> tmMessageSubscriptions = MultimapBuilder.<DeviceTmMessage, ActorRef>hashKeys().arrayListValues().build();

    @Override
    public void onReceive(Object o) throws Exception
    {
        if (o instanceof TmMessage)
        {
            TmMessage tmMessage = (TmMessage)o;
            tmMessageSubscriptions.get(new DeviceTmMessage(tmMessage.getDeviceGuid(), tmMessage.getMessage())).stream()
                    .forEach((a) -> a.tell(tmMessage, self()));
        }
        else if (o instanceof TmMessageSubscribe)
        {
            TmMessageSubscribe subscribe = (TmMessageSubscribe)o;
            tmMessageSubscriptions.put(new DeviceTmMessage(subscribe.getDeviceGuid(), subscribe.getMessage()), sender());
        }
        else
        {
            unhandled(o);
        }
    }
}
