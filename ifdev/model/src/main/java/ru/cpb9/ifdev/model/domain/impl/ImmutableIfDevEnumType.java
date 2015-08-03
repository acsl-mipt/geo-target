package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.IfDevEnumConstant;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import com.google.common.collect.ImmutableSet;
import ru.cpb9.ifdev.model.domain.type.IfDevEnumType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevEnumType extends AbstractImmutableIfDevType implements IfDevEnumType
{
    @NotNull
    private final IfDevMaybeProxy<IfDevType> baseType;
    @NotNull
    private final Set<IfDevEnumConstant> constants;

    public static IfDevEnumType newInstance(@NotNull Optional<IfDevName> name,
                                            @NotNull IfDevNamespace namespace,
                                            @NotNull IfDevMaybeProxy<IfDevType> baseType,
                                            @NotNull Optional<String> info,
                                            @NotNull Set<IfDevEnumConstant> constants)
    {
        return new ImmutableIfDevEnumType(name, namespace, baseType, info, constants);
    }

    private ImmutableIfDevEnumType(@NotNull Optional<IfDevName> name,
                                  @NotNull IfDevNamespace namespace,
                                  @NotNull IfDevMaybeProxy<IfDevType> baseType,
                                  @NotNull Optional<String> info,
                                  @NotNull Set<IfDevEnumConstant> constants)
    {
        super(name, namespace, info);
        this.baseType = baseType;
        this.constants = ImmutableSet.copyOf(constants);
    }

    @NotNull
    @Override
    public IfDevMaybeProxy<IfDevType> getBaseType()
    {
        return baseType;
    }

    @NotNull
    @Override
    public Set<IfDevEnumConstant> getConstants()
    {
        return constants;
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevEnumType{name=%s, namespace=%s, baseType=%s, info=%s, constants=%s}",
                name, namespace, baseType, info, constants);
    }
}
