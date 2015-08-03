package ru.cpb9.ifdev.model.domain.type;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevTypeVisitor<T, E extends Throwable>
{
    T visit(@NotNull IfDevPrimitiveType primitiveType) throws E;

    T visit(@NotNull IfDevSubType subType) throws E;

    T visit(@NotNull IfDevEnumType enumType) throws E;

    T visit(@NotNull IfDevArrayType arrayType) throws E;

    T visit(@NotNull IfDevStructType structType) throws E;

    T visit(@NotNull IfDevAliasType typeAlias) throws E;
}
