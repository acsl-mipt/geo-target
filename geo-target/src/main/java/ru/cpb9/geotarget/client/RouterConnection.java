package ru.cpb9.geotarget.client;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface RouterConnection<M> extends AutoCloseable
{
    void sendMessage(@NotNull M message);
    @NotNull
    M receiveMessageBlocking();
    @NotNull
    Optional<M> receiveMessageNonblocking();
}
