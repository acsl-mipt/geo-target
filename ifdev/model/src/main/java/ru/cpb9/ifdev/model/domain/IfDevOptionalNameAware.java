package ru.cpb9.ifdev.model.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface IfDevOptionalNameAware extends IfDevElement
{
    @NotNull
    Optional<IfDevName> getOptionalName();
}
