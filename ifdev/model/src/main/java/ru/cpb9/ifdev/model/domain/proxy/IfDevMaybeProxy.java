package ru.cpb9.ifdev.model.domain.proxy;

import ru.cpb9.ifdev.model.domain.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Artem Shein
 */
public interface IfDevMaybeProxy<T extends IfDevReferenceable> extends IfDevElement
{
    IfDevResolvingResult<T> resolve(@NotNull IfDevRegistry registry, @NotNull Class<T> cls);

    boolean isProxy();

    default boolean isResolved()
    {
        return !isProxy();
    }

    @NotNull
    T getObject();

    @NotNull
    IfDevProxy<T> getProxy();
}
