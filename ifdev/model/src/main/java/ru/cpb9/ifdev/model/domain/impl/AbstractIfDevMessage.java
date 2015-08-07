package ru.cpb9.ifdev.model.domain.impl;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public abstract class AbstractIfDevMessage extends AbstractImmutableIfDevOptionalInfoAware implements
        IfDevMessage
{
    public AbstractIfDevMessage(@NotNull Optional<String> info)
    {
        super(info);
    }
}
