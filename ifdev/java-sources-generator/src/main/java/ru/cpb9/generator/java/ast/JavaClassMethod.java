package ru.cpb9.generator.java.ast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author Artem Shein
 */
public class JavaClassMethod implements JavaAstElement
{
    @NotNull
    private final JavaVisibility visibility;
    private final boolean isStatic;
    @NotNull
    private final JavaType returnType;
    @NotNull
    private final String name;
    @NotNull
    private final List<JavaMethodArgument> arguments;
    @NotNull
    private final List<JavaStatement> statements;

    public JavaClassMethod(@NotNull JavaVisibility visibility, boolean isStatic, @NotNull JavaType returnType,
                           @NotNull String name, @NotNull List<JavaMethodArgument> arguments,
                           @NotNull List<JavaStatement> statements)
    {
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.returnType = returnType;
        this.name = name;
        this.arguments = arguments;
        this.statements = statements;
    }

    @Override
    public void generate(@NotNull JavaGeneratorState state, @NotNull Appendable appendable) throws IOException
    {
        visibility.generate(state, appendable);
        if (isStatic)
        {
            appendable.append(" static");
        }
        appendable.append(" ");
        returnType.generate(state, appendable);
        appendable.append(" ").append(name).append("(");
        if (!arguments.isEmpty())
        {
            boolean isFirst = true;
            for (JavaMethodArgument argument : arguments)
            {
                if (isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    appendable.append(", ");
                }
                argument.generate(state, appendable);
            }
        }
        appendable.append(")");
        state.startBlock();

        if (!statements.isEmpty())
        {
            boolean isFirst = true;
            for (JavaStatement statement : statements)
            {
                if (isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    state.eol();
                }
                state.indent();
                statement.generate(state, appendable);
                appendable.append(";");
            }
        }

        state.finishBlock();
    }
}
