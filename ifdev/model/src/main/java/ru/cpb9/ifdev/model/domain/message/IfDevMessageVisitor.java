package ru.cpb9.ifdev.model.domain.message;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevMessageVisitor<T, E extends Throwable>
{
    T visit(@NotNull IfDevEventMessage eventMessage) throws E;
    T visit(@NotNull IfDevStatusMessage statusMessage) throws E;
    T visit(@NotNull IfDevDynamicStatusMessage dynamicStatusMessage) throws E;
}
