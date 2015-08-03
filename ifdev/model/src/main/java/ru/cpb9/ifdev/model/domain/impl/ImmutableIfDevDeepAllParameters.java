package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevDeepAllParameters implements IfDevMessageParameter
{
    public static final ImmutableIfDevDeepAllParameters INSTANCE = new ImmutableIfDevDeepAllParameters();

    @NotNull
    @Override
    public String getValue()
    {
        return "*.*";
    }

    @Override
    public String toString()
    {
        return "ImmutableIfDevAllParameters{value=*}";
    }

    private ImmutableIfDevDeepAllParameters()
    {

    }
}
