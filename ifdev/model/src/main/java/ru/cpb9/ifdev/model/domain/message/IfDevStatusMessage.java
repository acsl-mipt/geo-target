package ru.cpb9.ifdev.model.domain.message;

/**
 * @author Artem Shein
 */
public interface IfDevStatusMessage extends IfDevMessage
{
    default <T, E extends Throwable> T accept(IfDevMessageVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }
}
