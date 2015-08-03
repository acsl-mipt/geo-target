package ru.cpb9.ifdev.idea.plugin;

import ru.cpb9.ifdev.modeling.TransformationMessage;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
class IfDevTransformationMessage implements TransformationMessage
{
    @NotNull
    private final String text;
    @NotNull
    private final Level level;

    public IfDevTransformationMessage(@NotNull Level level, @NotNull String msg, @NotNull Object... params)
    {
        this.level = level;
        this.text = String.format(msg, params);
    }

    @NotNull
    @Override
    public String getText()
    {
        return text;
    }

    @NotNull
    @Override
    public Level getLevel()
    {
        return level;
    }
}
