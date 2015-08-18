package ru.cpb9.generator.java.ast;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class JavaTypeApplication implements JavaAstElement
{
    @NotNull
    private final String type;
    @NotNull
    private final List<JavaTypeApplication> genericParameters;

    public JavaTypeApplication(@NotNull Class<?> cls)
    {
        this(cls.getCanonicalName(), new ArrayList<>());
    }

    public JavaTypeApplication(@NotNull Class<?> cls, @NotNull JavaTypeApplication... genericParameters)
    {
        this(cls.getCanonicalName(), Lists.newArrayList(genericParameters));
    }

    public JavaTypeApplication(@NotNull String type, @NotNull ArrayList<JavaTypeApplication> genericParameters)
    {
        this.type = type;
        this.genericParameters = genericParameters;
    }

    @Override
    public void generate(@NotNull Appendable appendable) throws IOException
    {
        appendable.append(type);
        if (!genericParameters.isEmpty())
        {
            appendable.append("<");
            boolean isFirst = true;
            for (JavaTypeApplication parameter: genericParameters)
            {
                if (isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    appendable.append(", ");
                }
                parameter.generate(appendable);
            }
            appendable.append(">");
        }
    }
}
