package ru.cpb9.ifdev.model.domain.proxy;

import ru.cpb9.ifdev.model.domain.IfDevReferenceable;
import ru.cpb9.ifdev.modeling.ResolvingResult;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface IfDevResolvingResult<T extends IfDevReferenceable> extends ResolvingResult
{
    Optional<T> getResolvedObject();
}
