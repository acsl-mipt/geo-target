package ru.cpb9.ifdev.model.domain.type;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Artem Shein
 */
public interface IfDevStructType extends IfDevType
{
    @NotNull
    List<IfDevStructField> getFields();

    @Override
    default <T, E extends Throwable> T accept(@NotNull IfDevTypeVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }
}
