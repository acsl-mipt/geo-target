package ru.cpb9.code.generation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * @author Artem Shein
 */
public interface Generator<C>
{
    /**
     * @throws GenerationException
     */
    void generate();

    @NotNull
    Optional<C> getConfiguration();
}
