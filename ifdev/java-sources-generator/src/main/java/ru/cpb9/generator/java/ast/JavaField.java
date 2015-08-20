package ru.cpb9.generator.java.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class JavaField implements JavaAstElement
{
    @NotNull
    private final JavaVisibility visibility;
    private final boolean isStatic;
    private final boolean isFinal;
    @NotNull
    private final JavaType type;
    @NotNull
    private final String name;
    @Nullable
    private final JavaClassMethodCallExpr value;

    public JavaField(@NotNull JavaVisibility visibility, boolean isStatic, boolean isFinal,
                     @NotNull JavaType type,
                     @NotNull String name,
                     @Nullable JavaClassMethodCallExpr value)
    {
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public JavaField(@NotNull JavaVisibility visibility, boolean isStatic, boolean isFinal, @NotNull JavaType type,
                     @NotNull String name)
    {
        this(visibility, isStatic, isFinal, type, name, null);
    }

    @Override
    public void generate(@NotNull JavaGeneratorState state, @NotNull Appendable appendable) throws IOException
    {
        visibility.generate(state, appendable);
        appendable.append(" ");
        if (isStatic)
        {
            appendable.append("static ");
        }
        if (isFinal)
        {
            appendable.append("final ");
        }
        type.generate(state,  appendable);
        appendable.append(" ").append(name);
        if (value != null)
        {
            appendable.append(" = ");
            value.generate(state, appendable);
        }
        appendable.append(";");
    }
}
