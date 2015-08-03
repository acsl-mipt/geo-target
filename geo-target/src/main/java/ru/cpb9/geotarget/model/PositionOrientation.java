package ru.cpb9.geotarget.model;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Kuchuk.
 */


public class PositionOrientation
{
    @NotNull
    private Position position;
    @NotNull
    private Angle heading;
    @NotNull
    private Angle pitch;
    @NotNull
    private Angle roll;

    public PositionOrientation(@NotNull Position position, @NotNull Angle heading, @NotNull Angle pitch, @NotNull Angle roll)
    {
        this.position = position;
        this.heading = heading;
        this.pitch = pitch;
        this.roll = roll;
    }

    public PositionOrientation()
    {
        this(Position.ZERO, Angle.ZERO, Angle.ZERO, Angle.ZERO);
    }

    @NotNull
    public Position getPosition() {
        return position;
    }

    @NotNull
    public Angle getHeading() {
        return heading;
    }

    @NotNull
    public Angle getPitch() {
        return pitch;
    }

    @NotNull
    public Angle getRoll() {
        return roll;
    }

    public void setPosition(@NotNull Position position) {
        this.position = position;
    }

    public void setHeading(double heading) {
        this.heading = Angle.fromDegrees(heading);
    }

    public void setPitch(double pitch) {
        this.pitch = Angle.fromDegrees(pitch);
    }

    public void setRoll(double roll) {
        this.roll = Angle.fromDegrees(roll);
    }
}
