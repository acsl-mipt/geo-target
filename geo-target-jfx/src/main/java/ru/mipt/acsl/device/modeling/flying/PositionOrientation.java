package ru.mipt.acsl.device.modeling.flying;

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
    private Orientation orientation;

    public PositionOrientation(@NotNull Position position, @NotNull Orientation orientation)
    {
        this.position = position;
        this.orientation = orientation;
    }

    public PositionOrientation()
    {
        this(Position.ZERO, Orientation.ZERO);
    }

    @NotNull
    public Position getPosition() {
        return position;
    }

    @NotNull
    public Angle getHeading() {
        return orientation.getHeading();
    }

    @NotNull
    public Angle getPitch() {
        return orientation.getPitch();
    }

    @NotNull
    public Angle getRoll() {
        return orientation.getRoll();
    }

    public void setPosition(@NotNull Position position) {
        this.position = position;
    }

    public void setHeadingDeg(double headingDeg)
    {
        this.orientation.setHeading(Angle.fromDegrees(headingDeg));
    }

    public void setPitchDeg(double pitchDeg)
    {
        this.orientation.setPitch(Angle.fromDegrees(pitchDeg));
    }

    public void setRollDeg(double rollDeg)
    {
        this.orientation.setRoll(Angle.fromDegrees(rollDeg));
    }
}
