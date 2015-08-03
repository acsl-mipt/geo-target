package ru.cpb9.geotarget.client.akka.message;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author Artem Shein
 */
public interface PersistentSubscribe<T> extends Serializable
{
    @NotNull
    T getUnsubscribeMessage();
}
