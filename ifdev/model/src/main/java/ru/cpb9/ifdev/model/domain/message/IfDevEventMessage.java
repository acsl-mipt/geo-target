package ru.cpb9.ifdev.model.domain.message;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevEventMessage extends IfDevMessage
{
    default <T, E extends Throwable> T accept(@NotNull IfDevMessageVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }
}
