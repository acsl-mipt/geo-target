package ru.cpb9.ifdev.model.domain;

import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

/**
 * @author Artem Shein
 */
public interface IfDevProxy<T extends IfDevReferenceable>
{
    IfDevResolvingResult<T> resolve(@NotNull IfDevRegistry registry, @NotNull Class<T> cls);

    @NotNull
    URI getUri();
}
