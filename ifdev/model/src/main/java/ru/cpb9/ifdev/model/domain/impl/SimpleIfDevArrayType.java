package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.ArraySize;
import ru.cpb9.ifdev.model.domain.type.IfDevArrayType;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class SimpleIfDevArrayType extends AbstractIfDevType implements IfDevArrayType
{
    @NotNull
    private final IfDevMaybeProxy<IfDevType> baseType;
    @NotNull
    private final ArraySize size;

    public static IfDevArrayType newInstance(@NotNull Optional<IfDevName> name, @NotNull IfDevNamespace namespace,
                                             @NotNull IfDevMaybeProxy<IfDevType> baseType,
                                             @NotNull Optional<String> info,
                                             @NotNull ArraySize size)
    {
        return new SimpleIfDevArrayType(name, namespace, baseType, info, size);
    }

    private SimpleIfDevArrayType(@NotNull Optional<IfDevName> name, @NotNull IfDevNamespace namespace,
                                 @NotNull IfDevMaybeProxy<IfDevType> baseType,
                                 @NotNull Optional<String> info, @NotNull ArraySize size)
    {
        super(name, namespace, info);
        this.baseType = baseType;
        this.size = size;
    }

    @NotNull
    @Override
    public ArraySize getSize()
    {
        return size;
    }

    @NotNull
    @Override
    public IfDevMaybeProxy<IfDevType> getBaseType()
    {
        return baseType;
    }

    @Override
    public String toString()
    {
        return String.format("SimpleIfDevArrayType{name=%s, namespace=%s, baseType=%s, size=%s, info=%s}",
                name, namespace.getFqn(), baseType, size, info);
    }

}
