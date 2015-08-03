package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.message.IfDevEventMessage;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevEventMessage extends AbstractImmutableIfDevMessage implements IfDevEventMessage
{
    @NotNull
    public static IfDevEventMessage newInstance(@NotNull IfDevName name, int id, @NotNull Optional<String> info,
                                                @NotNull List<IfDevMessageParameter> parameters)
    {
        return new ImmutableIfDevEventMessage(name, id, info, parameters);
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevEventMessage{name=%s, id=%s, info=%s, parameters=%s}",
                name, id, info, parameters);
    }

    private ImmutableIfDevEventMessage(@NotNull IfDevName name, int id, @NotNull Optional<String> info,
                                       @NotNull List<IfDevMessageParameter> parameters)
    {
        super(name, id, info, parameters);
    }
}
