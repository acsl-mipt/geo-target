package ru.mipt.acsl.device.modeling.flying;

import gov.nasa.worldwind.geom.Angle;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class Orientation
{
    @NotNull
    public static final Orientation ZERO = new Orientation(Angle.ZERO, Angle.ZERO, Angle.ZERO);

    @NotNull
    private Angle heading;
    @NotNull
    private Angle pitch;
    @NotNull
    private Angle roll;

    public Orientation(@NotNull Angle heading, @NotNull Angle pitch, @NotNull Angle roll)
    {
        this.heading = heading;
        this.pitch = pitch;
        this.roll = roll;
    }

    public double getHeadingDeg()
    {
        return heading.degrees;
    }

    @NotNull
    public Angle getHeading()
    {
        return heading;
    }

    @NotNull
    public Angle getPitch()
    {
        return pitch;
    }

    @NotNull
    public Angle getRoll()
    {
        return roll;
    }

    public void setHeading(@NotNull Angle heading)
    {
        this.heading = heading;
    }

    public void setPitch(@NotNull Angle pitch)
    {
        this.pitch = pitch;
    }

    public void setRoll(@NotNull Angle roll)
    {
        this.roll = roll;
    }

    @NotNull
    public static Orientation fromDegrees(double headingDeg, double pitchDeg, double rollDeg)
    {
        return new Orientation(Angle.fromDegrees(headingDeg), Angle.fromDegrees(pitchDeg), Angle.fromDegrees(rollDeg));
    }
}
