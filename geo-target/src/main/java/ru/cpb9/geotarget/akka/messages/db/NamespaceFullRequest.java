package ru.cpb9.geotarget.akka.messages.db;

import ru.cpb9.geotarget.akka.messages.Message;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class NamespaceFullRequest extends Message
{
    @NotNull
    private String fqn;

    public NamespaceFullRequest(long id, @NotNull LocalDateTime dateTime, @NotNull String sender,
                                   @NotNull String receiver, @NotNull MessageKind kind,
                                   @NotNull Optional<Long> requestId, @NotNull String fqn)
    {
        super(id, dateTime, sender, receiver, kind, requestId);
        this.fqn = fqn;
    }

    @NotNull
    public String getFqn()
    {
        return fqn;
    }
}
