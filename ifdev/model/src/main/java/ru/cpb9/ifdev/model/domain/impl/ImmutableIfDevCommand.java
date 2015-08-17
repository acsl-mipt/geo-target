package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevCommand;
import ru.cpb9.ifdev.model.domain.IfDevCommandArgument;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevCommand extends AbstractIfDevOptionalInfoAware implements IfDevCommand
{
    @NotNull
    private final List<IfDevCommandArgument> arguments;
    @NotNull
    private final IfDevName name;
    private int id;

    @NotNull
    public static IfDevCommand newInstance(@NotNull IfDevName name, int id, @NotNull Optional<String> info,
                                           @NotNull List<IfDevCommandArgument> arguments)
    {
        return new ImmutableIfDevCommand(name, id, info, arguments);
    }

    private ImmutableIfDevCommand(@NotNull IfDevName name, int id, @NotNull Optional<String> info,
                                 @NotNull List<IfDevCommandArgument> arguments)
    {
        super(info);
        this.name = name;
        this.id = id;
        this.arguments = arguments;
    }

    @Override
    public int getId()
    {
        return id;
    }

    @NotNull
    @Override
    public List<IfDevCommandArgument> getArguments()
    {
        return arguments;
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
        return String.format("ImmutableIfDevCommand{name=%s, arguments=%s, info=%s}", name, arguments, info);
    }
}
