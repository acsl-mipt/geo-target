package ru.cpb9.generator.java.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    protected final List<JavaConstructor> constructors;
    @NotNull
    protected final List<JavaField> fields = new ArrayList<>();
    @NotNull
    protected final List<JavaClassMethod> methods = new ArrayList<>();
    @NotNull
    private final List<String> genericArguments;
    @NotNull
    private final List<AbstractJavaBaseClass> innerClasses;
    @NotNull
    protected JavaVisibility visibility = JavaVisibility.PACKAGE_PRIVATE;
    @Nullable
    protected JavaType extendsClass;

    public AbstractJavaBaseClass(@NotNull String packageFqn, @NotNull String name,
                                 @NotNull List<String> genericArguments,
                                 @Nullable JavaType extendsClass, @NotNull List<AbstractJavaBaseClass> innerClasses,
                                 @NotNull List<JavaConstructor> constructors)
    {
        this.packageFqn = packageFqn;
        this.name = name;
        this.genericArguments = genericArguments;
        this.extendsClass = extendsClass;
        this.innerClasses = innerClasses;
        this.constructors = constructors;
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

    @NotNull
    public List<AbstractJavaBaseClass> getInnerClasses()
    {
        return innerClasses;
    }

    @NotNull
    public List<JavaField> getFields()
    {
        return fields;
    }

    @NotNull
    public List<JavaClassMethod> getMethods()
    {
        return methods;
    }

    @Nullable
    public JavaType getExtendsClass()
    {
        return extendsClass;
    }

    public void setExtendsClass(@Nullable JavaType extendsClass)
    {
        this.extendsClass = extendsClass;
    }

    @NotNull
    public List<JavaConstructor> getConstructors()
    {
        return constructors;
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
