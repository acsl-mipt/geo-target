package ru.cpb9.ifdev.model.domain.impl;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.IfDevElement;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevElementWrapper<T> implements IfDevElement
{
    private final T value;

    public static <T> ImmutableIfDevElementWrapper<T> newInstance(T value)
    {
        return new ImmutableIfDevElementWrapper<>(value);
    }

    public T getValue()
    {
        return value;
    }

    private ImmutableIfDevElementWrapper(@NotNull T value)
    {
        this.value = value;
    }
}
