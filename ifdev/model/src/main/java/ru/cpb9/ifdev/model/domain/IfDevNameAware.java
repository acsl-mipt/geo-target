package ru.cpb9.ifdev.model.domain;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface IfDevNameAware extends IfDevOptionalNameAware
{
    @NotNull
    default IfDevName getName()
    {
        return getOptionalName().get();
    }
}
