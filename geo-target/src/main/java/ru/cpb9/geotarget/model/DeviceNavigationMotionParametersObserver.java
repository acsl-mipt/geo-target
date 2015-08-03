package ru.cpb9.geotarget.model;

import ru.cpb9.geotarget.NullUtils;
import ru.cpb9.geotarget.TmStatus;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValueBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem Shein
 */
public class DeviceNavigationMotionParametersObserver extends ObservableValueBase<DeviceNavigationMotionParametersObserver>
{
    private static final Map<Device, DeviceNavigationMotionParametersObserver> MAP = new HashMap<>();

    @NotNull
    private final Device device;
    @Nullable
    private DoubleBinding latitude;
    @Nullable
    private DoubleBinding longitude;
    @Nullable
    private DoubleBinding altitude;
    @Nullable
    private DoubleBinding heading;
    @Nullable
    private DoubleBinding roll;
    @Nullable
    private DoubleBinding pitch;

    public static DeviceNavigationMotionParametersObserver getInstanceForDevice(@NotNull Device device)
    {
        return MAP.computeIfAbsent(device, k -> new DeviceNavigationMotionParametersObserver(device));
    }

    private DeviceNavigationMotionParametersObserver(@NotNull Device device)
    {
        this.device = device;
        bindParameters();
    }

    private void bindParameters()
    {
        TmParameter latitude = device.getTraitStatusOrNull(TmStatus.NAVIGATION_MOTION_LATITUDE);
        TmParameter longitude = device.getTraitStatusOrNull(TmStatus.NAVIGATION_MOTION_LONGITUDE);
        TmParameter altitude = device.getTraitStatusOrNull(TmStatus.NAVIGATION_MOTION_ALTITUDE);
        TmParameter heading = device.getTraitStatusOrNull(TmStatus.NAVIGATION_MOTION_HEADING);
        TmParameter roll = device.getTraitStatusOrNull(TmStatus.NAVIGATION_MOTION_ROLL);
        TmParameter pitch = device.getTraitStatusOrNull(TmStatus.NAVIGATION_MOTION_PITCH);
        if (NullUtils.isNotNull(latitude, longitude, altitude, heading, roll, pitch))
        {
            this.latitude = new TmParameterDoubleValueBinding(latitude);
            this.longitude = new TmParameterDoubleValueBinding(longitude);
            this.altitude = new TmParameterDoubleValueBinding(altitude);
            this.heading = new TmParameterDoubleValueBinding(heading);
            this.roll = new TmParameterDoubleValueBinding(roll);
            this.pitch = new TmParameterDoubleValueBinding(pitch);
        }
    }

    @Override
    @NotNull
    public DeviceNavigationMotionParametersObserver getValue()
    {
        return this;
    }

    private class TmParameterDoubleValueBinding extends DoubleBinding
    {
        private final TmParameter tmParameter;

        public TmParameterDoubleValueBinding(@NotNull TmParameter tmParameter)
        {
            this.tmParameter = tmParameter;
            bind(tmParameter.valueProperty());
        }

        @Override
        protected double computeValue()
        {
            //noinspection ConstantConditions
            return Double.parseDouble(tmParameter.getValue());
        }
    }
}
