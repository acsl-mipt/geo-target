package ru.cpb9.ifdev.model.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface IfDevUnit extends IfDevNameAware, IfDevOptionalInfoAware, IfDevReferenceable, IfDevNamespaceAware
{
    @NotNull
    Optional<String> getDisplay();

    @Override
    default <T, E extends Throwable> T accept(@NotNull IfDevReferenceableVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }
}
