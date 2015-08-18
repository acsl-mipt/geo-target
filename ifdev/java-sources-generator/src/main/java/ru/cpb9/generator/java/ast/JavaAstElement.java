package ru.cpb9.generator.java.ast;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.generation.Generatable;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public interface JavaAstElement extends Generatable
{
    default void eol(@NotNull Appendable appendable) throws IOException
    {
        appendable.append("\n");
    }
}
