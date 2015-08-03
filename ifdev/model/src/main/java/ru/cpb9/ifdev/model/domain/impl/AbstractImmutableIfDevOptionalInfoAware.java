package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevOptionalInfoAware;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public abstract class AbstractImmutableIfDevOptionalInfoAware implements IfDevOptionalInfoAware
{
    @NotNull
    protected final Optional<String> info;

    public AbstractImmutableIfDevOptionalInfoAware(@NotNull Optional<String> info)
    {
        this.info = info;
    }

    @NotNull
    @Override
    public Optional<String> getInfo()
    {
        return info;
    }
}
