package ru.cpb9.ifdev.model.domain;

import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.List;

/**
 * @author Artem Shein
 */
public interface IfDevRegistry
{
    @NotNull
    List<IfDevNamespace> getRootNamespaces();

    @NotNull
    <T extends IfDevReferenceable> IfDevResolvingResult<T> resolve(@NotNull URI uri, @NotNull Class<T> cls);
}
