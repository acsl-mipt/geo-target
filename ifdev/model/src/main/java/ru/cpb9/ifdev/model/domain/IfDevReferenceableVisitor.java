package ru.cpb9.ifdev.model.domain;

import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevReferenceableVisitor<T, E extends Throwable>
{
    T visit(@NotNull IfDevNamespace namespace) throws E;
    T visit(@NotNull IfDevType type) throws E;
    T visit(@NotNull IfDevComponent component) throws E;
    T visit(@NotNull IfDevUnit unit) throws E;
}
