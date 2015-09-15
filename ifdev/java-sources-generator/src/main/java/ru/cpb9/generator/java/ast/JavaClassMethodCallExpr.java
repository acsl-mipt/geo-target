package ru.cpb9.generator.java.ast;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Artem Shein
 */
public class JavaClassMethodCallExpr implements JavaExpr
{
    @NotNull
    private final JavaTypeApplication type;
    @NotNull
    private final String name;
    @NotNull
    private final ArrayList<JavaExpr> params;

    public JavaClassMethodCallExpr(@NotNull Class<?> cls, @NotNull String name)
    {
        this(new JavaTypeApplication(cls), name, new ArrayList<>());
    }

    public JavaClassMethodCallExpr(@NotNull JavaTypeApplication typeApplication, @NotNull String name,
                                   JavaExpr... params)
    {
        this(typeApplication, name, Lists.newArrayList(params));
    }

    public JavaClassMethodCallExpr(@NotNull JavaTypeApplication type, @NotNull String name,
                                   @NotNull ArrayList<JavaExpr> params)
    {
        this.type = type;
        this.name = name;
        this.params = params;
    }

    @Override
    public void generate(@NotNull JavaGeneratorState state, @NotNull Appendable appendable) throws IOException
    {
        type.generate(state, appendable);
        appendable.append(".").append(name).append("(");
        if (!params.isEmpty())
        {
            boolean isFirst = true;
            for (JavaExpr param : params)
            {
                if (isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    appendable.append(", ");
                }
                param.generate(state, appendable);
            }
        }
        appendable.append(")");
    }
}
