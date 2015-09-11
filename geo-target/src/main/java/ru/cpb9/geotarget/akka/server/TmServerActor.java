package ru.cpb9.geotarget.akka.server;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import ru.cpb9.geotarget.akka.messages.DeviceTmMessage;
import ru.cpb9.geotarget.akka.messages.TmMessage;
import ru.cpb9.geotarget.akka.messages.TmMessageSubscribe;
import ru.cpb9.geotarget.akka.messages.TmMessageUnsubscribe;

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
