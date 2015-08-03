package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.IfDevStructField;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import ru.cpb9.ifdev.model.domain.IfDevUnit;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevStructField extends AbstractImmutableIfDevOptionalInfoAware implements IfDevStructField
{
    @NotNull
    private final IfDevMaybeProxy<IfDevType> type;
    @NotNull
    private final Optional<IfDevMaybeProxy<IfDevUnit>> unit;
    @NotNull
    private final IfDevName name;

    public static IfDevStructField newInstance(@NotNull IfDevName name, @NotNull IfDevMaybeProxy<IfDevType> type,
                                               @NotNull Optional<IfDevMaybeProxy<IfDevUnit>> unit,
                                               @NotNull Optional<String> info)
    {
        return new ImmutableIfDevStructField(name, type, unit, info);
    }

    private ImmutableIfDevStructField(@NotNull IfDevName name, @NotNull IfDevMaybeProxy<IfDevType> type,
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
    public IfDevMaybeProxy<IfDevType> getType()
    {
        return type;
    }

    @NotNull
    @Override
    public Optional<IfDevMaybeProxy<IfDevUnit>> getUnit()
    {
        return unit;
    }

    @NotNull
    @Override
    public IfDevName getName()
    {
        return name;
    }

    @NotNull
    @Override
    public Optional<IfDevName> getOptionalName()
    {
        return Optional.of(name);
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevStructField{name=%s, type=%s, unit=%s, info=%s}", name, type, unit, info);
    }
}
