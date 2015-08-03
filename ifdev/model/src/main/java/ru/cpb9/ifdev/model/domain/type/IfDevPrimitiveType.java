package ru.cpb9.ifdev.model.domain.type;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevPrimitiveType extends IfDevType
{
    long getBitLength();

    @NotNull
    TypeKind getKind();

    @Override
    default <T, E extends Throwable> T accept(@NotNull IfDevTypeVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }
}
