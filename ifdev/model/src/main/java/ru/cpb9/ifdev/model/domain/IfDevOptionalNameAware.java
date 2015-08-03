package ru.cpb9.ifdev.model.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface IfDevOptionalNameAware
{
    @NotNull
    Optional<IfDevName> getOptionalName();
}
