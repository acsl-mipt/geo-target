package ru.mipt.acsl.geotarget.akka.server;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import ru.mipt.acsl.geotarget.akka.messages.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Artem Shein
 */
public class TmServerActor extends UntypedActor
{
    private final Set<ActorRef> allMessagesSubscriptions = new HashSet<>();
    private final Multimap<DeviceTmMessage, ActorRef> tmMessageSubscriptions = MultimapBuilder.<DeviceTmMessage, ActorRef>hashKeys().arrayListValues().build();

    @Override
    public void onReceive(Object o) throws Exception
    {
        if (o == AllMessagesSubscribe.INSTANCE)
        {
            allMessagesSubscriptions.add(getSender());
        }
        else if (o instanceof TmMessageValues)
        {
            TmMessageValues tmMessage = (TmMessageValues)o;
            ActorRef self = getSelf();
            allMessagesSubscriptions.stream().forEach(s -> s.tell(tmMessage, self));
            tmMessageSubscriptions.get(new DeviceTmMessage(tmMessage.getDeviceGuid(), tmMessage.getMessage())).stream()
                    .forEach((a) -> a.tell(tmMessage, self));
        }
        else if (o instanceof TmMessageSubscribe)
        {
            TmMessageSubscribe subscribe = (TmMessageSubscribe)o;
            tmMessageSubscriptions.put(subscribe, sender());
        }
        else if (o instanceof TmMessageUnsubscribe)
        {
            TmMessageUnsubscribe unsubscribe = (TmMessageUnsubscribe)o;
            tmMessageSubscriptions.get(unsubscribe).remove(sender());
        }
        else
        {
            unhandled(o);
        }
    }
}
