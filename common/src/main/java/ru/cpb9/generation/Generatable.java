package ru.cpb9.generation;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
@FunctionalInterface
public interface Generatable
{
    void generate(@NotNull Appendable appendable) throws IOException;
}
