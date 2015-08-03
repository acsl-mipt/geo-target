package ru.cpb9.ifdev.model.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Artem Shein
 */
public interface IfDevReferenceable extends IfDevNameAware
{
    @Nullable
    <T, E extends Throwable> T accept(@NotNull IfDevReferenceableVisitor<T, E> visitor) throws E;
}
