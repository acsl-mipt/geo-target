package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.type.IfDevPrimitiveType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class SimpleIfDevPrimitiveType extends AbstractIfDevType implements IfDevPrimitiveType
{
    @NotNull
    private final IfDevType.TypeKind kind;

    private final long bitLength;

    public static IfDevPrimitiveType newInstance(@NotNull Optional<IfDevName> name, @NotNull IfDevNamespace namespace,
                                                 @NotNull IfDevType.TypeKind kind, long bitLength,
                                                 @NotNull Optional<String> info)
    {
        return new SimpleIfDevPrimitiveType(name, namespace, kind, bitLength, info);
    }

    public static IfDevPrimitiveType newInstance(@NotNull Optional<IfDevName> name, @NotNull IfDevNamespace namespace,
                                                 @NotNull IfDevType.TypeKind kind, long bitLength,
                                            @Nullable String info)
    {
        return new SimpleIfDevPrimitiveType(name, namespace, kind, bitLength, Optional.ofNullable(info));
    }

    private SimpleIfDevPrimitiveType(@NotNull Optional<IfDevName> name, @NotNull IfDevNamespace namespace,
                                     @NotNull IfDevType.TypeKind kind, long bitLength,
                                     @NotNull Optional<String> info)
    {
        super(name, namespace, info);
        this.kind = kind;
        this.bitLength = bitLength;
    }

    @Override
    public long getBitLength()
    {
        return bitLength;
    }

    @NotNull
    @Override
    public IfDevType.TypeKind getKind()
    {
        return kind;
    }

    @NotNull
    @Override
    public String toString()
    {
        return String.format("SimpleIfDevPrimitiveType{name=%s, namespace=%s, kind=%s, bitLength=%s, info=%s}",
                name, namespace.getFqn(), kind.getName(), bitLength, info);
    }
}
