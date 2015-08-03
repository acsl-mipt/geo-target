package ru.cpb9.ifdev.model.domain;

import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevDomainModelResolver
{
    IfDevResolvingResult<IfDevReferenceable> resolve(@NotNull IfDevRegistry ifDevRegistry);
}
