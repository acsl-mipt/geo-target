package ru.cpb9.ifdev.model.domain.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import ru.cpb9.ifdev.model.domain.IfDevComponent;
import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import ru.cpb9.ifdev.model.domain.IfDevCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevComponent extends AbstractIfDevComponent
{
    @NotNull
    private final Optional<IfDevMaybeProxy<IfDevType>> baseType;
    @NotNull
    private final Set<IfDevMaybeProxy<IfDevComponent>> subComponents;
    @NotNull
    private final List<IfDevCommand> commands;
    @NotNull
    private final List<IfDevMessage> messages;
    @NotNull
    private final IfDevName name;
    @NotNull
    private final IfDevNamespace namespace;

    @NotNull
    public static IfDevComponent newInstance(@NotNull IfDevName name, @NotNull IfDevNamespace namespace,
                                      @NotNull Optional<IfDevMaybeProxy<IfDevType>> baseType,
                                      @NotNull Optional<String> info,
                                      @NotNull Set<IfDevMaybeProxy<IfDevComponent>> subComponents,
                                      @NotNull List<IfDevCommand> commands,
                                      @NotNull List<IfDevMessage> messages)
    {
        return new ImmutableIfDevComponent(name, namespace, baseType, info, subComponents, commands, messages);
    }

    private ImmutableIfDevComponent(@NotNull IfDevName name, @NotNull IfDevNamespace namespace,
                                   @NotNull Optional<IfDevMaybeProxy<IfDevType>> baseType,
                                   @NotNull Optional<String> info,
                                   @NotNull Set<IfDevMaybeProxy<IfDevComponent>> subComponents,
                                   @NotNull List<IfDevCommand> commands,
                                   @NotNull List<IfDevMessage> messages)
    {
        super(info);
        this.name = name;
        this.namespace = namespace;
        this.baseType = baseType;
        this.subComponents = ImmutableSet.copyOf(subComponents);
        this.commands = ImmutableList.copyOf(commands);
        this.messages = ImmutableList.copyOf(messages);
    }

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
    public Optional<IfDevName> getOptionalName()
    {
        return Optional.of(name);
    }

    @NotNull
    @Override
    public IfDevName getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevComponent{name=%s, baseType=%s, info=%s, subComponents=%s, commands=%s, messages=%s}",
                name, baseType, info, subComponents, commands, messages);
    }

    @NotNull
    @Override
    public IfDevNamespace getNamespace()
    {
        return namespace;
    }
}
