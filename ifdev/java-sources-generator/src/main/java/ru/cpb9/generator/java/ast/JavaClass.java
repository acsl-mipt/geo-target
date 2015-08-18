package ru.cpb9.generator.java.ast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class JavaClass implements JavaAstElement
{
    @NotNull
    private final String _package;
    @NotNull
    private final String name;
    @NotNull
    private List<JavaField> fields = new ArrayList<>();

    public JavaClass(@NotNull String _package, @NotNull String name)
    {
        this._package = _package;
        this.name = name;
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    @NotNull
    public String getPackage()
    {
        return _package;
    }

    @Override
    public void generate(@NotNull Appendable appendable) throws IOException
    {
        appendable.append("package ").append(_package).append(";");
        eol(appendable);
        eol(appendable);
        appendable.append("public class ").append(name);
        eol(appendable);
        appendable.append("{");
        eol(appendable);

        for (JavaField field : fields)
        {
            appendable.append("\t");
            field.generate(appendable);
            eol(appendable);
        }

        appendable.append("}");
        eol(appendable);
    }

    public List<JavaField> getFields()
    {
        return fields;
    }
}
