package ru.cpb9.device.modeling.flying;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Artem Shein
 */
public class Route
{
    private int name;
    @NotNull
    private SimplifiedRouteKind kind;
    private boolean isRing;
    private int crc16;
    private int maxLength;
    @NotNull
    private String info;
    @NotNull
    private List<RoutePoint> points;

    public Route(int name, @NotNull SimplifiedRouteKind kind, boolean isRing, int crc16, int maxLength, @NotNull String info,
                 @NotNull List<RoutePoint> points)
    {
        this.name = name;
        this.kind = kind;
        this.isRing = isRing;
        this.crc16 = crc16;
        this.maxLength = maxLength;
        this.info = info;
        this.points = points;
    }

    @NotNull
    public RoutePoint getPoint(int i)
    {
        return points.get(i);
    }

    public int size()
    {
        return points.size();
    }

    public boolean isRing()
    {
        return isRing;
    }
}
