package ru.cpb9.geotarget.akka.messages;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public abstract class Message implements Serializable
{
    private final long id;
    @NotNull
    private final LocalDateTime dateTime;
    @NotNull
    private final String sender;
    @NotNull
    private final String receiver;
    @NotNull
    private final MessageKind kind;
    @NotNull
    private final Optional<Long> requestId;

    protected Message(long id, @NotNull LocalDateTime dateTime, @NotNull String sender, @NotNull String receiver, @NotNull MessageKind kind,
                      @NotNull Optional<Long> requestId)
    {
        this.id = id;
        this.dateTime = dateTime;
        this.sender = sender;
        this.receiver = receiver;
        this.kind = kind;
        this.requestId = requestId;
    }

    @NotNull
    public Optional<Long> getRequestId()
    {
        return requestId;
    }

    @NotNull
    public MessageKind getKind()
    {
        return kind;
    }

    @NotNull
    public String getReceiver()
    {
        return receiver;
    }

    @NotNull
    public String getSender()
    {
        return sender;
    }

    @NotNull
    public LocalDateTime getDateTime()
    {
        return dateTime;
    }

    public long getId()
    {
        return id;
    }

    public enum MessageKind
    {
        REQ, REP, SUB, MULTICAST;
    }
}
