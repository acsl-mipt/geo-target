package ru.cpb9.ifdev.model.domain.proxy;

import ru.cpb9.ifdev.model.domain.IfDevReferenceable;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevMaybeProxy<T extends IfDevReferenceable>
{
    IfDevResolvingResult<T> resolve(@NotNull IfDevRegistry registry, @NotNull Class<T> cls);

    boolean isProxy();

    default boolean isResolved()
    {
        return !isProxy();
    }

    @NotNull
    T getObject();
}
