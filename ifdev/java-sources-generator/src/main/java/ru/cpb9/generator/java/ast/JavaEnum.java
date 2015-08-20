package ru.cpb9.generator.java.ast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class JavaEnum extends AbstractJavaBaseClass
{
    public static Builder newBuilder(@NotNull String packageFqn, @NotNull String enumName)
    {
        return new Builder(packageFqn, enumName);
    }

    @Override
    public void generate(@NotNull JavaGeneratorState state, @NotNull Appendable appendable) throws IOException
    {
        generateVisibility(state, appendable);
        appendable.append("enum ").append(name);
        state.startBlock();

        state.finishBlock();
    }

    public static class Builder extends AbstractJavaBaseClass.Builder
    {
        @NotNull
        private List<String> genericArguments = new ArrayList<>();

        public Builder(@NotNull String packageFqn, @NotNull String enumName)
        {
            super(packageFqn, enumName);
        }

        @NotNull
        @Override
        public JavaEnum build()
        {
            return new JavaEnum(packageFqn, name, genericArguments, innerClasses);
        }
    }

    private JavaEnum(@NotNull String packageFqn, @NotNull String name, @NotNull List<String> genericArguments,
                     @NotNull List<AbstractJavaBaseClass> innerClasses)
    {
        super(packageFqn, name, genericArguments, innerClasses);
    }
}
