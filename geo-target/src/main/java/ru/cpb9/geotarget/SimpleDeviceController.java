package ru.cpb9.geotarget;

import ru.cpb9.geotarget.model.Device;
import ru.cpb9.geotarget.ui.controls.WorldWindNode;
import ru.cpb9.geotarget.ui.layers.DevicesLayer;
import gov.nasa.worldwind.geom.Position;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class SimpleDeviceController implements DeviceController
{
    private final DeviceRegistry deviceRegistry;
    private final WorldWindNode worldWind;

    public SimpleDeviceController(
            @NotNull DeviceRegistry deviceRegistry, @NotNull WorldWindNode worldWindNode)
    {
        this.deviceRegistry = deviceRegistry;
        this.worldWind = worldWindNode;
    }

    @NotNull
    @Override
    public DeviceRegistry getDeviceRegistry()
    {
        return deviceRegistry;
    }

    @NotNull
    @Override
    public WorldWindNode getWorldWind()
    {
        return worldWind;
    }

    @Override
    public void navigateToActiveDevice()
    {
//        Device activeDevice = deviceRegistry.getActiveDevice().getValue();
//        if (activeDevice == null)
//        {
//            return;
//        }
//        Double latitude = activeDevice.getTraitStatusValueAsDoubleOrNull(TmStatus.NAVIGATION_MOTION_LATITUDE);
//        Double longitude = activeDevice.getTraitStatusValueAsDoubleOrNull(TmStatus.NAVIGATION_MOTION_LONGITUDE);
//        Double altitude = activeDevice.getTraitStatusValueAsDoubleOrNull(TmStatus.NAVIGATION_MOTION_ALTITUDE);
//        if (NullUtils.isNotNull(latitude, longitude, altitude))
//        {
//            @SuppressWarnings("ConstantConditions")
//            double elevation = (altitude + 1000) * 10;
//            //noinspection ConstantConditions
//            worldWind.getPanel().getView().goTo(Position.fromDegrees(latitude, longitude, elevation), elevation);
//            worldWind.getPanel().getModel().getLayers().stream().filter(layer -> layer instanceof DevicesLayer).forEach(layer -> {
//                ((DevicesLayer) layer).getDeviceCones().stream()
//                        .forEach(cone -> cone.setConeSize(elevation / 10));
//            });
//        }
    }

    @Override
    public void requestRepaint()
    {
        worldWind.getPanel().repaint();
    }
}
