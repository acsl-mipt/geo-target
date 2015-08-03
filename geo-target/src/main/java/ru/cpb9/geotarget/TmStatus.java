package ru.cpb9.geotarget;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public enum TmStatus
{
    NAVIGATION_MOTION_LATITUDE(Trait.NAVIGATION_MOTION, "latitude"),
    NAVIGATION_MOTION_LONGITUDE(Trait.NAVIGATION_MOTION, "longitude"),
    NAVIGATION_MOTION_ALTITUDE(Trait.NAVIGATION_MOTION, "altitude"),
    NAVIGATION_MOTION_HEADING(Trait.NAVIGATION_MOTION, "heading"),
    NAVIGATION_MOTION_ROLL(Trait.NAVIGATION_MOTION, "roll"),
    NAVIGATION_MOTION_PITCH(Trait.NAVIGATION_MOTION, "pitch"),

    NAVIGATION_ROUTE_IS_RING(Trait.NAVIGATION_ROUTES, "isRing"),
    NAVIGATION_ROUTE_ROUTE_KIND(Trait.NAVIGATION_ROUTES, "routeKind"),
    NAVIGATION_ROUTE_POINTS_LENGTH(Trait.NAVIGATION_ROUTES, "pointsLength"),
    NAVIGATION_ROUTE_NEXT_POINT(Trait.NAVIGATION_ROUTES, "nextPoint"),

    ROUTE_POINT_ALTITUDE(Trait.NAVIGATION_ROUTES_ROUTE, "altitude"),
    ROUTE_POINT_LATITUDE(Trait.NAVIGATION_ROUTES_ROUTE, "latitude"),
    ROUTE_POINT_SPEED(Trait.NAVIGATION_ROUTES_ROUTE, "speed"),
    ROUTE_POINT_LONGITUDE(Trait.NAVIGATION_ROUTES_ROUTE, "longitude");

    @NotNull
    public static final ImmutableList<TmStatus> NAVIGATION_MOTION_MOTION_STATUSES = ImmutableList
            .of(NAVIGATION_MOTION_LATITUDE, NAVIGATION_MOTION_LONGITUDE, NAVIGATION_MOTION_ALTITUDE,
                    NAVIGATION_MOTION_HEADING, NAVIGATION_MOTION_PITCH, NAVIGATION_MOTION_ROLL);
    @NotNull
    private final Trait trait;
    @NotNull
    private final String name;

    TmStatus(@NotNull Trait trait, @NotNull String name)
    {
        this.trait = trait;
        this.name = name;
    }

    @NotNull
    public Trait getTrait()
    {
        return trait;
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    @Override
    @NotNull
    public String toString()
    {
        return trait + "." + name;
    }
}
