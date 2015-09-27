package ru.cpb9.device.modeling.flying;

/**
 * @author Artem Shein
 */
public class Coordinates
{
    private double latitudeDeg;
    private double longitudeDeg;
    private double altitudeM;

    public Coordinates(double latDeg, double lonDeg, double altitudeM)
    {
        this.latitudeDeg = latDeg;
        this.longitudeDeg = lonDeg;
        this.altitudeM = altitudeM;
    }

    public double getLatitudeDeg()
    {
        return latitudeDeg;
    }

    public void setLatitudeDeg(double latitudeDeg)
    {
        this.latitudeDeg = latitudeDeg;
    }

    public double getLongitudeDeg()
    {
        return longitudeDeg;
    }

    public void setLongitudeDeg(double longitudeDeg)
    {
        this.longitudeDeg = longitudeDeg;
    }

    public double getAltitudeM()
    {
        return altitudeM;
    }

    public void setAltitudeM(double altitudeM)
    {
        this.altitudeM = altitudeM;
    }

    public void addAltitudeM(double altitudeM)
    {
        this.altitudeM += altitudeM;
    }

    public void addLatitudeDeg(double latitudeDeg)
    {
        this.latitudeDeg += latitudeDeg;
    }

    public void addLongitudeDeg(double longitudeDeg)
    {
        this.longitudeDeg += longitudeDeg;
    }
}
