package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevMessageParameter implements IfDevMessageParameter
{
    @NotNull
    private final String text;

    public static IfDevMessageParameter newInstance(@NotNull String text)
    {
        return new ImmutableIfDevMessageParameter(text);
    }

    private ImmutableIfDevMessageParameter(@NotNull String text)
    {
        this.text = text;
    }

    @Override
    @NotNull
    public String getValue()
    {
        return text;
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevMessageParameter{text=%s}", text);
    }
}
