package ru.cpb9.ifdev.model.domain.impl;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.IfDevCommand;
import ru.cpb9.ifdev.model.domain.IfDevComponent;
import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.IfDevType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Artem Shein
 */
public class SimpleIfDevComponent extends AbstractIfDevOptionalInfoAware implements IfDevComponent
{
    @NotNull
    private final IfDevName name;
    @NotNull
    private final Optional<Integer> id;
    @NotNull
    private IfDevNamespace namespace;
    @NotNull
    private final Optional<IfDevMaybeProxy<IfDevType>> baseType;
    @NotNull
    private final Set<IfDevMaybeProxy<IfDevComponent>> subComponents;
    @NotNull
    private final List<IfDevCommand> commands;
    @NotNull
    private final List<IfDevMessage> messages;

    @NotNull
    @Override
    public Optional<IfDevMaybeProxy<IfDevType>> getBaseType()
    {
        return baseType;
    }

    @NotNull
    @Override
    public Set<IfDevMaybeProxy<IfDevComponent>> getSubComponents()
    {
        return subComponents;
    }

    @NotNull
    @Override
    public List<IfDevCommand> getCommands()
    {
        return commands;
    }

    @NotNull
    @Override
    public List<IfDevMessage> getMessages()
    {
        return messages;
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

    @NotNull
    @Override
    public Optional<String> getInfo()
    {
        return info;
    }

    @NotNull
    @Override
    public Optional<IfDevName> getOptionalName()
    {
        return Optional.of(name);
    }

    public static IfDevComponent newInstance(@NotNull IfDevName name, @NotNull IfDevNamespace namespace,
                                             @NotNull Optional<Integer> id,
                                             @NotNull Optional<IfDevMaybeProxy<IfDevType>> baseType,
                                             @NotNull Optional<String> info,
                                             @NotNull Set<IfDevMaybeProxy<IfDevComponent>> subComponents,
                                             @NotNull List<IfDevCommand> commands,
                                             @NotNull List<IfDevMessage> messages)
    {
        return new SimpleIfDevComponent(name, namespace, id, baseType, info, subComponents, commands, messages);
    }

    private SimpleIfDevComponent(@NotNull IfDevName name, @NotNull IfDevNamespace namespace,
                                 @NotNull Optional<Integer> id, @NotNull Optional<IfDevMaybeProxy<IfDevType>> baseType,
                                 @NotNull Optional<String> info,
                                 @NotNull Set<IfDevMaybeProxy<IfDevComponent>> subComponents,
                                 @NotNull List<IfDevCommand> commands, @NotNull List<IfDevMessage> messages)
    {
        super(info);
        this.name = name;
        this.namespace = namespace;
        this.id = id;
        this.baseType = baseType;
        this.subComponents = subComponents;
        this.commands = commands;
        this.messages = messages;
    }
}
