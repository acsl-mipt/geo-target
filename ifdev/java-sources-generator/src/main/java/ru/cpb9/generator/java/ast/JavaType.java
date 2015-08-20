package ru.cpb9.generator.java.ast;

import org.jetbrains.annotations.NotNull;
import ru.cpb9.generation.Generatable;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public interface JavaType extends JavaAstElement
{
    enum Primitive implements JavaType
    {
        BOOLEAN("boolean"), FLOAT("float"), DOUBLE("double"), BYTE("byte"), SHORT("short"), INT("int"), LONG("long");

        @NotNull
        private final String name;

        Primitive(@NotNull String name)
        {
            this.name = name;
        }

        @Override
        public void generate(@NotNull JavaGeneratorState state, @NotNull Appendable appendable) throws IOException
        {
            appendable.append(name);
        }
    }

    enum Std implements JavaType
    {
        BIG_INTEGER("java.math.BigInteger");

        @NotNull
        private final String typeFqn;

        Std(@NotNull String typeFqn)
        {
            this.typeFqn = typeFqn;
        }

        @Override
        public void generate(@NotNull JavaGeneratorState state, @NotNull Appendable appendable) throws IOException
        {
            appendable.append(typeFqn);
        }
    }
}
