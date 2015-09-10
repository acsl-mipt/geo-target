package ru.cpb9.ifdev.model.domain.type;

import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevArrayType extends IfDevType
{
    @NotNull
    ArraySize getSize();

    @NotNull
    IfDevMaybeProxy<IfDevType> getBaseType();

    default boolean isFixedSize()
    {
        ArraySize size = getSize();
        long maxLength = size.getMaxLength();
        return size.getMinLength() == maxLength && maxLength != 0;
    }

    @Override
    default <T, E extends Throwable> T accept(@NotNull IfDevTypeVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }
}
