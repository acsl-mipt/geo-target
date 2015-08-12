package ru.cpb9.ifdev.parser;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class IfDevSourceProviderConfiguration
{
    @NotNull
    private String resourcePath;

    @NotNull
    public String getResourcePath()
    {
        return "ru/cpb9/ifdev";
    }

    public void setResourcePath(@NotNull String resourcePath)
    {
        this.resourcePath = resourcePath;
    }
}
