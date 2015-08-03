package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevAllParameters implements IfDevMessageParameter
{

    public static final ImmutableIfDevAllParameters INSTANCE = new ImmutableIfDevAllParameters();

    @NotNull
    @Override
    public String getValue()
    {
        return "*";
    }

    @Override
    public String toString()
    {
        return "ImmutableIfDevAllParameters{value=*}";
    }

    private ImmutableIfDevAllParameters()
    {

    }
}
