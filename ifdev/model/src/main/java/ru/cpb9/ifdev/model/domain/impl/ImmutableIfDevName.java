package ru.cpb9.ifdev.model.domain.impl;

import com.google.common.base.Preconditions;
import ru.cpb9.ifdev.model.domain.IfDevName;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public final class ImmutableIfDevName implements IfDevName
{
    @NotNull
    private final String value;

    public static IfDevName newInstanceFromSourceName(@NotNull String name)
    {
        return new ImmutableIfDevName(IfDevName.mangleName(name));
    }

    public static IfDevName newInstanceFromMangledName(@NotNull String value)
    {
        return new ImmutableIfDevName(value);
    }

    @NotNull
    @Override
    public String asString()
    {
        return value;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof IfDevName))
        {
            return false;
        }
        IfDevName name = (IfDevName)o;
        return value.equals(name.asString());
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevName{value=%s}", value);
    }

    private ImmutableIfDevName(@NotNull String value)
    {
        Preconditions.checkArgument(!value.contains(" ") && !value.contains("^"),
                "invalid mangled name '%s'", value);
        this.value = value;
    }
}
