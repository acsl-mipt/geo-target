package ru.cpb9.geotarget.server.db;

import akka.actor.UntypedActor;
import ru.cpb9.geotarget.akka.messages.Message;
import ru.cpb9.geotarget.nanomsg.NanomsgServiceName;
import ru.cpb9.geotarget.akka.messages.ErrorResponse;
import ru.cpb9.geotarget.akka.messages.db.NamespaceFullRequest;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.domain.impl.SimpleIfDevRegistry;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.provider.IfDevSqlProvider;
import ru.cpb9.ifdev.model.provider.IfDevSqlProviderConfiguration;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class LocalDbServerActor extends UntypedActor
{
    @NotNull
    private final URL resource;
    @NotNull
    private IfDevRegistry registry = SimpleIfDevRegistry.newInstance();

    public LocalDbServerActor(@NotNull URL dbResource)
    {
        resource = dbResource;
    }

    @Override
    public void onReceive(Object o)
    {
        if (o instanceof NamespaceFullRequest)
        {
            NamespaceFullRequest namespaceFullRequest = (NamespaceFullRequest)o;
            self().tell(new ErrorResponse(0, LocalDateTime.now(), NanomsgServiceName.LOCAL_DB_SERVER.getName(), namespaceFullRequest.getSender(),
                    Message.MessageKind.REQ, Optional.of((long) 1), namespaceFullRequest.getId(), Optional.of("Fuck off")), getSender());
        }
        else
        {
            unhandled(o);
        }
    }

    @Override
    public void preStart()
    {
        IfDevSqlProviderConfiguration config = new IfDevSqlProviderConfiguration();
        config.setConnectionUrl("jdbc:sqlite::resource:" + resource);
        registry = new IfDevSqlProvider().provide(config);
    }
}
