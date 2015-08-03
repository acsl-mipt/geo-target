package ru.cpb9.geotarget;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public enum Trait
{
    NAVIGATION_MOTION("Navigation.Motion"), DEVICE("Device"), NAVIGATION_ROUTES("Navigation.Routes"), NAVIGATION_ROUTES_ROUTE("Navigation.Routes.Route");

    @NotNull
    private final String name;

    @NotNull
    public String getName()
    {
        return name;
    }

    @Override
    @NotNull
    public String toString()
    {
        return name;
    }

    Trait(@NotNull String name)
    {
        this.name = name;
    }
}
