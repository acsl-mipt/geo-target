package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class SimpleIfDevUnit extends AbstractIfDevOptionalInfoAware implements IfDevUnit
{
    @NotNull
    private final Optional<String> display;
    @NotNull
    private final IfDevName name;
    @NotNull
    private IfDevNamespace namespace;

    @NotNull
    public static IfDevUnit newInstance(@NotNull IfDevName name, @NotNull IfDevNamespace namespace,
                                        @NotNull Optional<String> display,
                                        @NotNull Optional<String> info)
    {
        return new SimpleIfDevUnit(name, namespace, display, info);
    }

    @NotNull
    public static IfDevUnit newInstance(@NotNull IfDevName name, @NotNull IfDevNamespace namespace,
                                        @Nullable String display, @Nullable String info)
    {
        return newInstance(name, namespace, Optional.ofNullable(display), Optional.ofNullable(info));
    }

    private SimpleIfDevUnit(@NotNull IfDevName name, @NotNull IfDevNamespace namespace,
                            @NotNull Optional<String> display,
                            @NotNull Optional<String> info)
    {
        super(info);
        this.name = name;
        this.namespace = namespace;
        this.display = display;
    }

    @NotNull
    @Override
    public Optional<String> getDisplay()
    {
        return display;
    }

    @NotNull
    @Override
    public IfDevName getName()
    {
        return name;
    }

    @NotNull
    @Override
    public Optional<IfDevName> getOptionalName()
    {
        return Optional.of(name);
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("Unit{name=%s, display=%s, info=%s}", name, display, info);
    }

    @NotNull
    @Override
    public IfDevNamespace getNamespace()
    {
        return namespace;
    }

    @Override
    public void setNamespace(@NotNull IfDevNamespace namespace)
    {
        this.namespace = namespace;
    }
}