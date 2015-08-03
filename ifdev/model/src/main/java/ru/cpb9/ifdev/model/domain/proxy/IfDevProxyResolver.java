package ru.cpb9.ifdev.model.domain.proxy;

import ru.cpb9.ifdev.model.domain.IfDevReferenceable;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

/**
 * @author Artem Shein
 */
public interface IfDevProxyResolver
{
    @NotNull
    <T extends IfDevReferenceable> IfDevResolvingResult<T> resolve(@NotNull IfDevRegistry registry, @NotNull URI uri,
                                                       @NotNull Class<T> cls);
}
