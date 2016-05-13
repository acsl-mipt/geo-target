package ru.mipt.acsl.geotarget;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;
import javafx.collections.ObservableList;
import ru.mipt.acsl.device.modeling.flying.PositionOrientation;
import ru.mipt.acsl.geotarget.ui.DeviceCone;
import ru.mipt.acsl.geotarget.ui.controls.WorldWindNode;
import ru.mipt.acsl.geotarget.ui.layers.DevicesLayer;

/**
 * @author Artem Shein
 */
class DeviceControllerImpl implements DeviceController
{

    private final DeviceRegistry deviceRegistry;

    private final WorldWindNode worldWind;

    DeviceControllerImpl(DeviceRegistry deviceRegistry, WorldWindNode worldWind)
    {
        this.deviceRegistry = deviceRegistry;
        this.worldWind = worldWind;
    }

    @Override
    public DeviceRegistry getDeviceRegistry()
    {
        return deviceRegistry;
    }

    @Override
    public WorldWindNode getWorldWind()
    {
        return worldWind;
    }

    @Override
    public void navigateToActiveDevice()
    {
        deviceRegistry.getActiveDevice().ifPresent(activeDevice -> {
            ObservableList<PositionOrientation> devicePositions = activeDevice.getDevicePositions();
            PositionOrientation positionOrientation = devicePositions.get(devicePositions.size() - 1);
            Position position = positionOrientation.getPosition();
            double latitude = position.latitude.degrees;
            double longitude = position.longitude.degrees;
            double altitude = position.elevation;
            double elevation = (altitude + 1000) * 10;
            worldWind.getPanel().getView().goTo(Position.fromDegrees(latitude, longitude, elevation), elevation);
            for (Layer layer : worldWind.getPanel().getModel().getLayers())
            {
                if (layer instanceof DevicesLayer)
                {
                    DevicesLayer devicesLayer = (DevicesLayer) layer;
                    for (DeviceCone deviceCone : devicesLayer.getDeviceCones())
                    {
                        deviceCone.setConeSize(elevation / 10);
                    }
                }
            }
        });
    }

    @Override
    public void requestRepaint()
    {
        worldWind.getPanel().redraw();
    }
}
