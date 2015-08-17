package ru.cpb9.ifdev.model.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Artem Shein
 */
public interface IfDevFqn extends IfDevElement
{
    @NotNull
    List<IfDevName> getParts();

    @NotNull
    String asString();

    @NotNull
    default IfDevName getLast()
    {
        List<IfDevName> parts = getParts();
        return parts.get(parts.size() - 1);
    }

    @NotNull
    IfDevFqn copyDropLast();

    int size();

    default boolean isEmpty()
    {
        return size() == 0;
    }
}
