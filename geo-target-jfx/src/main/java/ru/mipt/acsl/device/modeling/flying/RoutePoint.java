package ru.mipt.acsl.device.modeling.flying;

/**
 * @author Artem Shein
 */
public class RoutePoint extends Coordinates
{
    private double speedMps;
    private long flags = 0;

    public RoutePoint(double latDeg, double lonDeg, double altitudeM, double speedMps)
    {
        this(latDeg, lonDeg, altitudeM, speedMps, 0);
    }

    public RoutePoint(double latDeg, double lonDeg, double altitudeM, double speedMps, int flags)
    {
        super(latDeg, lonDeg, altitudeM);
        this.speedMps = speedMps;
        this.flags = flags;
    }

    public double getSpeedMps()
    {
        return speedMps;
    }
}
