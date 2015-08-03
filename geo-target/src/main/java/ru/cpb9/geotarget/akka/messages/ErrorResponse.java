package ru.cpb9.geotarget.akka.messages;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ErrorResponse extends Message
{
    private final long messageId;
    @NotNull
    private Optional<String> description;

    public ErrorResponse(long id, @NotNull LocalDateTime dateTime, @NotNull String sender,
                            @NotNull String receiver, @NotNull MessageKind kind,
                            @NotNull Optional<Long> requestId, long messageId, @NotNull Optional<String> description)
    {
        super(id, dateTime, sender, receiver, kind, requestId);
        this.messageId = messageId;
        this.description = description;
    }

    public long getMessageId()
    {
        return messageId;
    }

    @NotNull
    public Optional<String> getDescription()
    {
        return description;
    }
}
