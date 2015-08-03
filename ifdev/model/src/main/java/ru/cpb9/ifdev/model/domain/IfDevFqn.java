package ru.cpb9.ifdev.model.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Artem Shein
 */
public interface IfDevFqn
{
    @NotNull
    List<IfDevName> getParts();
}
