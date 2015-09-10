package ru.cpb9.ifdev.model.domain.impl;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.type.IfDevNativeType;
import ru.cpb9.ifdev.model.domain.type.IfDevTypeVisitor;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevBerType extends AbstractIfDevType implements IfDevNativeType
{
    public static final IfDevName MANGLED_NAME = ImmutableIfDevName.newInstanceFromMangledName("ber");

    public static IfDevNativeType newInstance(@NotNull Optional<IfDevName> name,
                                              @NotNull IfDevNamespace namespace,
                                              @NotNull Optional<String> info)
    {
        return new ImmutableIfDevBerType(name, namespace, info);
    }

    private ImmutableIfDevBerType(@NotNull Optional<IfDevName> name,
                                  @NotNull IfDevNamespace namespace,
                                  @NotNull Optional<String> info)
    {
        super(name, namespace, info);
    }

    @Override
    public <T, E extends Throwable> T accept(@NotNull IfDevTypeVisitor<T, E> visitor) throws E
    {
        return visitor.visit(this);
    }
}
