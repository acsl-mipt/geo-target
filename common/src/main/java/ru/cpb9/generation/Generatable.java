package ru.cpb9.generation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * @author Artem Shein
 */
@FunctionalInterface
public interface Generatable<S>
{
    void generate(@Nullable S generatorState, @NotNull Appendable appendable) throws IOException;
}
