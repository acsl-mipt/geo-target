package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevOptionalNameAndOptionalInfoAware;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class AbstractIfDevOptionalNameAndOptionalInfoAware extends AbstractIfDevOptionalInfoAware implements
        IfDevOptionalNameAndOptionalInfoAware
{
    @NotNull
    protected Optional<IfDevName> name;

    public AbstractIfDevOptionalNameAndOptionalInfoAware(@NotNull Optional<IfDevName> name,
                                                         @NotNull Optional<String> info)
    {
        super(info);
        this.name = name;
    }

    @NotNull
    @Override
    public Optional<IfDevName> getOptionalName()
    {
        return name;
    }
}
