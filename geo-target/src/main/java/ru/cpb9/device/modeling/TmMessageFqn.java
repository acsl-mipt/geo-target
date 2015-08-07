package ru.cpb9.device.modeling;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public enum TmMessageFqn
{
    MOTION_ALL("ru.mipt.acsl.Motion.all");

    @NotNull
    private final String fqn;

    TmMessageFqn(@NotNull String fqn)
    {
        this.fqn = fqn;
    }

    @NotNull
    @Override
    public String toString()
    {
        return fqn;
    }
}
