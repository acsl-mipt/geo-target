package ru.cpb9.generator.java.ast;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class JavaClass extends AbstractJavaBaseClass
{
    public JavaClass(@NotNull String _package, @NotNull String name, @NotNull List<String> genericArguments,
                     @Nullable JavaType extendsClass,
                     @NotNull List<JavaConstructor> constructors, @NotNull List<AbstractJavaBaseClass> innerClasses)
    {
        super( _package, name, genericArguments, extendsClass, innerClasses, constructors);
    }

    @Override
    public void generate(@NotNull JavaGeneratorState state, @NotNull Appendable appendable) throws IOException
    {
        generateVisibility(state, appendable);
        appendable.append("class ").append(name);
        generateGenericArguments(state, appendable);

        if (extendsClass != null)
        {
            appendable.append(" extends ");
            extendsClass.generate(state, appendable);
        }

        state.startBlock();

        for (JavaField field : fields)
        {
            state.indent();
            field.generate(state, appendable);
            state.eol();
        }

        for (JavaConstructor constructor : constructors)
        {
            state.indent();
            constructor.generate(state, appendable);
            state.eol();
        }

        for (JavaClassMethod method : methods)
        {
            state.indent();
            method.generate(state, appendable);
            state.eol();
        }

        generateInnerClasses(state, appendable);

        state.finishBlock();
    }

    public static Builder newBuilder(@NotNull String packageFqn, @NotNull String name)
    {
        return new Builder(packageFqn, name);
    }

    public static class Builder
    {
        @NotNull
        private final String packageFqn;
        @NotNull
        private final String name;
        @Nullable
        private JavaType extendsClass;
        @NotNull
        private List<JavaConstructor> constructors = new ArrayList<>();
        @NotNull
        private List<String> genericArguments = new ArrayList<>();
        @NotNull
        private List<AbstractJavaBaseClass> innerClasses = new ArrayList<>();

        public Builder(@NotNull String packageFqn, @NotNull String name)
        {
            this.packageFqn = packageFqn;
            this.name = name;
        }


        public JavaClass build()
        {
            return new JavaClass(packageFqn, name, genericArguments, extendsClass, constructors, innerClasses);
        }

        @NotNull
        public Builder extendsClass(@NotNull String packageFqn, @NotNull String name,
                                                                    @NotNull JavaType... genericParameters)
        {
            return extendsClass(packageFqn + "." + name, genericParameters);
        }

        public Builder extendsClass(@NotNull String fqn, @NotNull JavaType... genericParameters)
        {
            Preconditions.checkState(extendsClass == null);
            extendsClass =  new JavaTypeApplication(fqn, genericParameters);
            return this;
        }

        public Builder constuctor(@NotNull List<JavaMethodArgument> arguments,
                                                                  @NotNull JavaStatement... statements)
        {
            return constuctor(arguments, Lists.newArrayList(statements));
        }

        public Builder constuctor(@NotNull List<JavaMethodArgument> arguments,
                                  @NotNull List<JavaStatement> statements)
        {
            constructors.add(new JavaConstructor(JavaVisibility.PUBLIC, name, arguments, Lists.newArrayList(statements)));
            return this;
        }

        public Builder genericArgument(@NotNull String name)
        {
            genericArguments.add(name);
            return this;
        }

        public Builder extendsClass(@NotNull JavaType extendsType)
        {
            this.extendsClass = extendsType;
            return this;
        }

        public Builder innerClass(@NotNull JavaClass innerClass)
        {
            this.innerClasses.add(innerClass);
            return this;
        }
    }
}
