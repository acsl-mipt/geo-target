package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.type.IfDevEnumConstant;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevEnumConstant extends AbstractImmutableIfDevOptionalInfoAware implements IfDevEnumConstant
{
    @NotNull
    private final IfDevName name;
    @NotNull
    private final String value;

    @NotNull
    public static IfDevEnumConstant newInstance(@NotNull IfDevName name, @NotNull String value, @NotNull Optional<String> info)
    {
        return new ImmutableIfDevEnumConstant(name, value, info);
    }

    @NotNull
    @Override
    public IfDevName getName()
    {
        return name;
    }

    @NotNull
    @Override
    public String getValue()
    {
        return value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof IfDevEnumConstant))
        {
            return false;
        }
        IfDevEnumConstant c = (IfDevEnumConstant)o;
        return this == c || (name.equals(c.getName()) && value.equals(c.getValue()));
    }

    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevEnumConstant{name=%s, value=%s, info=%s}", name, value, info);
    }

    private ImmutableIfDevEnumConstant(@NotNull IfDevName name, @NotNull String value, @NotNull Optional<String> info)
    {
        super(info);
        this.name = name;
        this.value = value;
    }
}
