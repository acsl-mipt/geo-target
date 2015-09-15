package ru.cpb9.geotarget;

import javafx.collections.ObservableList;
import ru.cpb9.device.modeling.flying.PositionOrientation;
import ru.cpb9.geotarget.model.Device;
import ru.cpb9.geotarget.ui.controls.WorldWindNode;
import ru.cpb9.geotarget.ui.layers.DevicesLayer;
import gov.nasa.worldwind.geom.Position;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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
        Optional<Device> activeDevice = deviceRegistry.activeDeviceProperty().getValue();
        if (!activeDevice.isPresent())
        {
            return;
        }
        ObservableList<PositionOrientation> devicePositions = activeDevice.get().getDevicePositions();
        if (!devicePositions.isEmpty())
        {
            PositionOrientation positionOrientation =
                    devicePositions.get(devicePositions.size() - 1);
            Position position = positionOrientation.getPosition();
            double latitude = position.latitude.degrees;
            double longitude = position.longitude.degrees;
            double altitude = position.elevation;
            @SuppressWarnings("ConstantConditions")
            double elevation = (altitude + 1000) * 10;
            //noinspection ConstantConditions
            worldWind.getPanel().getView().goTo(Position.fromDegrees(latitude, longitude, elevation), elevation);
            worldWind.getPanel().getModel().getLayers().stream().filter(layer -> layer instanceof DevicesLayer)
                    .forEach(layer -> {
                        ((DevicesLayer) layer).getDeviceCones().stream()
                                .forEach(cone -> cone.setConeSize(elevation / 10));
                    });
        }
    }

    @Override
    public void requestRepaint()
    {
        worldWind.getPanel().repaint();
    }
}
