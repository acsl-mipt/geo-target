package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import ru.cpb9.ifdev.model.domain.IfDevUnit;
import ru.cpb9.ifdev.model.domain.IfDevCommandArgument;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevCommandArgument extends AbstractImmutableIfDevOptionalInfoAware implements
        IfDevCommandArgument
{
    @NotNull
    private final IfDevMaybeProxy<IfDevType> type;

    @NotNull
    private final Optional<IfDevMaybeProxy<IfDevUnit>> unit;
    @NotNull
    private final IfDevName name;

    public static IfDevCommandArgument newInstance(@NotNull IfDevName name, @NotNull IfDevMaybeProxy<IfDevType> type,
                                                   @NotNull Optional<IfDevMaybeProxy<IfDevUnit>> unit,
                                                   @NotNull Optional<String> info)
    {
        return new ImmutableIfDevCommandArgument(name, type, unit, info);
    }

    private ImmutableIfDevCommandArgument(@NotNull IfDevName name, @NotNull IfDevMaybeProxy<IfDevType> type,
                                          @NotNull Optional<IfDevMaybeProxy<IfDevUnit>> unit,
                                          @NotNull Optional<String> info)
    {
        super(info);
        this.name = name;
        this.type = type;
        this.unit = unit;
    }

    @NotNull
    @Override
    public Optional<IfDevMaybeProxy<IfDevUnit>> getUnit()
    {
        return unit;
    }

    @NotNull
    @Override
    public IfDevMaybeProxy<IfDevType> getType()
    {

        return type;
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
        return String.format("ImmutableIfDevCommandArgument{name=%s, type=%s, unit=%s, info=%s}", name, type, unit, info);
    }
}
