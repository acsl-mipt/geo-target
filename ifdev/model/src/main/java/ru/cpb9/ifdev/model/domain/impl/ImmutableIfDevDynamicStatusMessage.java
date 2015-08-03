package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.message.IfDevDynamicStatusMessage;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevDynamicStatusMessage extends AbstractImmutableIfDevMessage
        implements IfDevDynamicStatusMessage
{
    @NotNull
    public static IfDevDynamicStatusMessage newInstance(@NotNull IfDevName name, int id, @NotNull Optional<String> info,
                                                        @NotNull List<IfDevMessageParameter> parameters)
    {
        return new ImmutableIfDevDynamicStatusMessage(name, id, info, parameters);
    }


    @NotNull
    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevDynamicStatusMessage{name=%s, id=%s, parameters=%s, info=%s}",
                name, id, parameters, info);
    }

    private ImmutableIfDevDynamicStatusMessage(@NotNull IfDevName name, int id, @NotNull Optional<String> info,
                                               @NotNull List<IfDevMessageParameter> parameters)
    {
        super(name, id, info, parameters);
    }
}
