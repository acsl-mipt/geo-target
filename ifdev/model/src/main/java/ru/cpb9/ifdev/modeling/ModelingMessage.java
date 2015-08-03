package ru.cpb9.ifdev.modeling;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public interface ModelingMessage
{
    enum Level
    {
        NOTICE, WARN, ERROR
    }

    @NotNull
    String getText();

    @NotNull
    Level getLevel();
}
