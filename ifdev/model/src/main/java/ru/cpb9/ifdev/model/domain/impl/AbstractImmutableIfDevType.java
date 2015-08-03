package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public abstract class AbstractImmutableIfDevType extends AbstractImmutableIfDevOptionalNameAndOptionalInfoAware implements IfDevType
{
    @NotNull
    protected final IfDevNamespace namespace;

    public AbstractImmutableIfDevType(
            @NotNull Optional<IfDevName> name,
            @NotNull IfDevNamespace namespace,
            @NotNull Optional<String> info)
    {
        super(name, info);
        this.namespace = namespace;
    }

    @NotNull
    @Override
    public IfDevNamespace getNamespace()
    {
        return namespace;
    }
}
