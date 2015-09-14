package ru.cpb9.device.modeling;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public enum TmMessageFqn
{
    MOTION_ALL("ru.mipt.acsl.Motion.all"),
    DEVICE_ALL("ru.mipt.acsl.Device.all"),
    MODE_ALL("ru.mipt.acsl.Mode.all"),
    DOG_POINT_ALL("ru.mipt.acsl.DogPoint.all"),
    ROUTE_ALL("ru.mipt.acsl.Route.all");

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
