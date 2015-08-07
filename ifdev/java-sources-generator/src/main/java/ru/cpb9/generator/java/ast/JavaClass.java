package ru.cpb9.generator.java.ast;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.code.generation.Generatable;
import ru.cpb9.ifdev.model.domain.IfDevFqn;

/**
 * @author Artem Shein
 */
public class JavaClass implements Generatable
{
    @NotNull
    private final String _package;
    @NotNull
    private final String className;

    public JavaClass(@NotNull String _package, @NotNull String className)
    {
        this._package = _package;
        this.className = className;
    }

    @Override
    public void generate(@NotNull Appendable appendable)
    {

    }
}
