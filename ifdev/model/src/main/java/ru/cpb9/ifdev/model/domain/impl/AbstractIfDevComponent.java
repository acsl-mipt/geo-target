package ru.cpb9.ifdev.model.domain.impl;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.IfDevComponent;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public abstract class AbstractIfDevComponent extends AbstractImmutableIfDevOptionalInfoAware implements IfDevComponent
{
    public AbstractIfDevComponent(@NotNull Optional<String> info)
    {
        super(info);
    }
}
