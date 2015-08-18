package ru.cpb9.generator.java.ast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public enum JavaVisibility implements JavaAstElement
{
    PUBLIC("public"), PROTECTED("protected"), PACKAGE_PRIVATE(""), PRIVATE("private");

    @NotNull
    private final String name;

    JavaVisibility(@NotNull String name)
    {
        this.name = name;
    }

    @Override
    public void generate(@NotNull Appendable appendable) throws IOException
    {
        appendable.append(name);
    }
}
