package ru.cpb9.generator.java.ast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class JavaConstructor implements JavaAstElement
{
    @NotNull
    private final JavaVisibility visibility;
    @NotNull
    private final String className;
    @NotNull
    private final List<JavaMethodArgument> arguments;
    @NotNull
    private final List<JavaStatement> statements;

    public JavaConstructor(@NotNull JavaVisibility visibility, @NotNull String className, @NotNull List<JavaMethodArgument> arguments,
                           @NotNull List<JavaStatement> statements)
    {
        this.visibility = visibility;
        this.className = className;
        this.arguments = arguments;
        this.statements = statements;
    }

    @Override
    public void generate(@NotNull JavaGeneratorState state, @NotNull Appendable appendable) throws IOException
    {
        visibility.generate(state, appendable);
        appendable.append(" ").append(className).append("(");
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
