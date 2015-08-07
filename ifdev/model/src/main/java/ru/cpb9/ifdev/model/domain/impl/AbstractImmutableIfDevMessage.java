package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevComponent;
import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public abstract class AbstractImmutableIfDevMessage extends AbstractIfDevMessage
{
    @NotNull
    protected final IfDevName name;
    protected final int id;
    @NotNull
    protected final List<IfDevMessageParameter> parameters;
    @NotNull
    protected final IfDevComponent component;

    @NotNull
    @Override
    public IfDevComponent getComponent()
    {
        return component;
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    @NotNull
    public IfDevName getName()
    {
        return name;
    }

    @Override
    @NotNull
    public List<IfDevMessageParameter> getParameters()
    {
        return parameters;
    }

    @NotNull
    @Override
    public Optional<IfDevName> getOptionalName()
    {
        return Optional.of(name);
    }


    protected AbstractImmutableIfDevMessage(@NotNull IfDevComponent component, @NotNull IfDevName name, int id,
                                            @NotNull Optional<String> info,
                                            @NotNull List<IfDevMessageParameter> parameters)
    {
        super(info);
        this.component = component;
        this.id = id;
        this.name = name;
        this.parameters = parameters;
    }
}
