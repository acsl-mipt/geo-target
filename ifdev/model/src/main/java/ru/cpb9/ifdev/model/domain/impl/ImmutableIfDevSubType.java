package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.type.IfDevSubType;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevSubType extends AbstractImmutableIfDevType implements IfDevSubType
{
    @NotNull
    private final IfDevMaybeProxy<IfDevType> baseType;

    public static IfDevSubType newInstance(@NotNull Optional<IfDevName> name,
                                           @NotNull IfDevNamespace namespace,
                                           @NotNull IfDevMaybeProxy<IfDevType> baseType,
                                           @NotNull Optional<String> info)
    {
        return new ImmutableIfDevSubType(name, namespace, baseType, info);
    }

    private ImmutableIfDevSubType(@NotNull Optional<IfDevName> name, @NotNull IfDevNamespace namespace,
                                  @NotNull IfDevMaybeProxy<IfDevType> baseType,
                                  @NotNull Optional<String> info)
    {
        super(name, namespace, info);
        this.baseType = baseType;
    }

    @NotNull
    @Override
    public IfDevMaybeProxy<IfDevType> getBaseType()
    {
        return baseType;
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevSubType{name=%s, namespace=%s, baseType=%s, info=%s}",
                name, namespace.getFqn(), baseType, info);
    }
}
