package ru.cpb9.ifdev.model.domain;

import ru.cpb9.ifdev.model.domain.impl.ImmutableIfDevName;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public final class IfDevConstants
{
    @NotNull
    public static final IfDevName SYSTEM_NAMESPACE_NAME = ImmutableIfDevName.newInstanceFromMangledName("ifdev");

    private IfDevConstants()
    {
    }
}
