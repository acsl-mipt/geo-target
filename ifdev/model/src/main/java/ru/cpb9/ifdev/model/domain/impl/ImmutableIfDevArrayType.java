package ru.cpb9.ifdev.model.domain.impl;

import ru.cpb9.ifdev.model.domain.IfDevName;
import ru.cpb9.ifdev.model.domain.IfDevNamespace;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.ArraySize;
import ru.cpb9.ifdev.model.domain.type.IfDevArrayType;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public class ImmutableIfDevArrayType extends AbstractImmutableIfDevType implements IfDevArrayType
{
    @NotNull
    private final IfDevMaybeProxy<IfDevType> baseType;
    @NotNull
    private final ArraySize size;

    public static IfDevArrayType newInstance(@NotNull Optional<IfDevName> name, @NotNull IfDevNamespace namespace,
                                             @NotNull IfDevMaybeProxy<IfDevType> baseType,
                                             @NotNull Optional<String> info,
                                             @NotNull ArraySize size)
    {
        return new ImmutableIfDevArrayType(name, namespace, baseType, info, size);
    }

    private ImmutableIfDevArrayType(@NotNull Optional<IfDevName> name, @NotNull IfDevNamespace namespace,
                                    @NotNull IfDevMaybeProxy<IfDevType> baseType,
                                    @NotNull Optional<String> info, @NotNull ArraySize size)
    {
        super(name, namespace, info);
        this.baseType = baseType;
        this.size = size;
    }

    @NotNull
    @Override
    public ArraySize getSize()
    {
        return size;
    }

    @NotNull
    @Override
    public IfDevMaybeProxy<IfDevType> getBaseType()
    {
        return baseType;
    }

    @Override
    public String toString()
    {
        return String.format("ImmutableIfDevArrayType{name=%s, namespace=%s, baseType=%s, size=%s, info=%s}",
                name, namespace.getFqn(), baseType, size, info);
    }

    public static class ImmutableArraySize implements ArraySize
    {
        private final long minLength;
        private final long maxLength;

        @NotNull
        public static ArraySize newInstance(long minLength, long maxLength)
        {
            return new ImmutableArraySize(minLength, maxLength);
        }

        @Override
        public long getMinLength()
        {
            return minLength;
        }

        @Override
        public long getMaxLength()
        {
            return maxLength;
        }

        @Override
        public String toString()
        {
            return String.format("ImmutableArraySize{minLength=%s, maxLength=%s}", minLength, maxLength);
        }

        private ImmutableArraySize(long minLength, long maxLength)
        {
            this.minLength = minLength;
            this.maxLength = maxLength;
        }
    }
}
