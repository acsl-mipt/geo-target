package ru.cpb9.device.modeling.flying;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class FlyingDeviceModelingParameters
{
    private double maxSpeedMps;
    private double maxAccelerationMpsps;
    private double takingOffAltitudeM;
    private double rotateSpeedDegps;
    private boolean isSignalBad;

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public double getTakingOffAltitudeM()
    {
        return takingOffAltitudeM;
    }

    public boolean isSignalBad()
    {
        return isSignalBad;
    }

    public double getRotateSpeedDegps()
    {
        return rotateSpeedDegps;
    }

    public double getMaxSpeedMps()
    {
        return maxSpeedMps;
    }

    public double getMaxAccelerationMpsps()
    {
        return maxAccelerationMpsps;
    }

    private FlyingDeviceModelingParameters(double maxSpeedMps, double maxAccelerationMpsps, double takingOffAltitudeM,
                                           double rotateSpeedDegps, boolean isSignalBad)
    {
        this.maxSpeedMps = maxSpeedMps;
        this.maxAccelerationMpsps = maxAccelerationMpsps;
        this.takingOffAltitudeM = takingOffAltitudeM;
        this.rotateSpeedDegps = rotateSpeedDegps;
        this.isSignalBad = isSignalBad;
    }

    public static final class Builder
    {
        private double maxSpeedMps = 60.;
        private double maxAccelerationMpsps = 20.;
        private double takingOffAltitudeM = 50.;
        private double rotateSpeedDegps = 30.;
        private boolean isSignalBad = false;

        public Builder()
        {
        }

        @NotNull
        public FlyingDeviceModelingParameters build()
        {
            return new FlyingDeviceModelingParameters(maxSpeedMps, maxAccelerationMpsps, takingOffAltitudeM, rotateSpeedDegps, isSignalBad);
        }
    }
}
