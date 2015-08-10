package ru.cpb9.generation;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class GenerationException extends RuntimeException
{
    public GenerationException(@NotNull Exception cause)
    {
        super(cause);
    }

    public GenerationException(@NotNull String msg)
    {
        super(msg);
    }
}