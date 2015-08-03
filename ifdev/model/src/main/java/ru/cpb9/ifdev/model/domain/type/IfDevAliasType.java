package ru.cpb9.ifdev.model.domain.type;

import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevAliasType extends IfDevType
{
    @Override
    default <T, E extends Throwable> T accept(@NotNull IfDevTypeVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }

    @NotNull
    IfDevMaybeProxy<IfDevType> getType();
}
