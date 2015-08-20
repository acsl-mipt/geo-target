package ru.cpb9.generator.java.ast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public abstract class AbstractJavaBaseClass implements JavaAstElement
{
    @NotNull
    protected final String packageFqn;
    @NotNull
    protected final String name;
    @NotNull
    private final List<String> genericArguments;
    @NotNull
    private final List<AbstractJavaBaseClass> innerClasses;
    @NotNull
    protected JavaVisibility visibility = JavaVisibility.PACKAGE_PRIVATE;

    public AbstractJavaBaseClass(@NotNull String packageFqn, @NotNull String name,
                                 @NotNull List<String> genericArguments, @NotNull List<AbstractJavaBaseClass> innerClasses)
    {
        this.packageFqn = packageFqn;
        this.name = name;
        this.genericArguments = genericArguments;
        this.innerClasses = innerClasses;
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    @NotNull
    public String getPackage()
    {
        return packageFqn;
    }

    protected void generateVisibility(@NotNull JavaGeneratorState state, @NotNull Appendable appendable) throws
            IOException
    {
        visibility.generate(state, appendable);
        if (visibility != JavaVisibility.PACKAGE_PRIVATE)
        {
            appendable.append(" ");
        }
    }

    protected void generateGenericArguments(@NotNull JavaGeneratorState state, @NotNull Appendable appendable) throws
            IOException
    {
        if (!genericArguments.isEmpty())
        {
            appendable.append("<");
            boolean isFirst = true;
            for (String genericArgument : genericArguments)
            {
                if (isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    appendable.append(", ");
                }
                appendable.append(genericArgument);
            }
            appendable.append(">");
        }
    }

    protected void generateInnerClasses(@NotNull JavaGeneratorState state, @NotNull Appendable appendable) throws
            IOException
    {
        if (!innerClasses.isEmpty())
        {
            for (AbstractJavaBaseClass cls : innerClasses)
            {
                state.indent();
                cls.generate(state, appendable);
            }
        }
    }

    public abstract static class Builder
    {
        @NotNull
        protected JavaVisibility visibility = JavaVisibility.PACKAGE_PRIVATE;
        @NotNull
        protected final String packageFqn;
        @NotNull
        protected final String name;
        protected List<AbstractJavaBaseClass> innerClasses = new ArrayList<>();

        public Builder(@NotNull String packageFqn, @NotNull String name)
        {
            this.packageFqn = packageFqn;
            this.name = name;
        }

        @NotNull
        public abstract AbstractJavaBaseClass build();
    }
}
