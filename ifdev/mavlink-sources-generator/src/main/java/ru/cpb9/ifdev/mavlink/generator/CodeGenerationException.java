package ru.cpb9.ifdev.mavlink.generator;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class CodeGenerationException extends RuntimeException
{
    public CodeGenerationException(@NotNull Exception cause)
    {
        super(cause);
    }

    public CodeGenerationException(@NotNull String msg)
    {
        super(msg);
    }
}
