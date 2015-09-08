package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.IfDevAliasType;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class SimpleIfDevAliasType extends AbstractIfDevOptionalInfoAware implements ru.cpb9.ifdev.model.domain.type.IfDevAliasType
{
    @NotNull
    private final IfDevName name;
    @NotNull
    private final IfDevMaybeProxy<IfDevType> type;
    @NotNull
    private IfDevNamespace namespace;

    public SimpleIfDevAliasType(@NotNull IfDevName name, @NotNull IfDevNamespace namespace,
                                @NotNull IfDevMaybeProxy<IfDevType> type, @NotNull Optional<String> info)
    {
        super(info);
        this.name = name;
        this.namespace = namespace;
        this.type = type;
    }

    @NotNull
    public static IfDevAliasType newInstance(@NotNull IfDevName name, @NotNull IfDevNamespace namespace,
                                             @NotNull IfDevMaybeProxy<IfDevType> type,
                                             @NotNull Optional<String> info)
    {
        return new SimpleIfDevAliasType(name, namespace, type, info);
    }

    @NotNull
    @Override
    public Optional<IfDevName> getOptionalName()
    {
        return Optional.of(name);
    }

    @NotNull
    @Override
    public IfDevMaybeProxy<IfDevType> getType()
    {
        return type;
    }

    @NotNull
    @Override
    public IfDevName getName()
    {

        return name;
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("SimpleIfDevTypeAlias{name=%s, namespace=%s, type=%s, info=%s}", name, namespace.getFqn(), type, info);
    }

    @NotNull
    @Override
    public IfDevNamespace getNamespace()
    {
        return namespace;
    }

    @Override
    public void setNamespace(@NotNull IfDevNamespace namespace)
    {
        this.namespace = namespace;
    }
}