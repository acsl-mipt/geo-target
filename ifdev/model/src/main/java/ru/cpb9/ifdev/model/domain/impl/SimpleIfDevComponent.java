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
public class SimpleIfDevComponent extends AbstractIfDevComponent
{
    @NotNull
    private final IfDevName name;
    @NotNull
    private final IfDevNamespace namespace;
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
        return null;
    }

    @NotNull
    @Override
    public Set<IfDevMaybeProxy<IfDevComponent>> getSubComponents()
    {
        return null;
    }

    @NotNull
    @Override
    public List<IfDevCommand> getCommands()
    {
        return null;
    }

    @NotNull
    @Override
    public List<IfDevMessage> getMessages()
    {
        return null;
    }

    @NotNull
    @Override
    public IfDevNamespace getNamespace()
    {
        return null;
    }

    @NotNull
    @Override
    public Optional<String> getInfo()
    {
        return null;
    }

    @NotNull
    @Override
    public Optional<IfDevName> getOptionalName()
    {
        return null;
    }

    public static IfDevComponent newInstance(@NotNull IfDevName name, @NotNull IfDevNamespace namespace,
                                             @NotNull Optional<IfDevMaybeProxy<IfDevType>> baseType,
                                             @NotNull Optional<String> info,
                                             @NotNull Set<IfDevMaybeProxy<IfDevComponent>> subComponents,
                                             @NotNull List<IfDevCommand> commands,
                                             @NotNull List<IfDevMessage> messages)
    {
        return new SimpleIfDevComponent(name, namespace, baseType, info, subComponents, commands, messages);
    }

    private SimpleIfDevComponent(@NotNull IfDevName name, @NotNull IfDevNamespace namespace,
                                 @NotNull Optional<IfDevMaybeProxy<IfDevType>> baseType,
                                 @NotNull Optional<String> info,
                                 @NotNull Set<IfDevMaybeProxy<IfDevComponent>> subComponents,
                                 @NotNull List<IfDevCommand> commands, @NotNull List<IfDevMessage> messages)
    {
        super(info);
        this.name = name;
        this.namespace = namespace;
        this.baseType = baseType;
        this.subComponents = subComponents;
        this.commands = commands;
        this.messages = messages;
    }
}
