package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevComponent;
import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import ru.cpb9.ifdev.model.domain.message.IfDevStatusMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevStatusMessage extends AbstractImmutableIfDevMessage implements IfDevStatusMessage
{
    @NotNull
    public static IfDevStatusMessage newInstance(@NotNull IfDevComponent component, @NotNull IfDevName name, int id,
                                                 @NotNull Optional<String> info,
                                                 @NotNull List<IfDevMessageParameter> parameters)
    {
        return new ImmutableIfDevStatusMessage(component, name, id, info, parameters);
    }

    private ImmutableIfDevStatusMessage(@NotNull IfDevComponent component, @NotNull IfDevName name, int id,
                                        @NotNull Optional<String> info,
                                        @NotNull List<IfDevMessageParameter> parameters)
    {
        super(component, name, id, info, parameters);
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevStatusMessage{name=%s, id=%s, info=%s, parameters=%s}", name, id, info, parameters);
    }
}
