package ru.cpb9.geotarget.ui.layers;

import ru.cpb9.geotarget.DeviceController;
import ru.cpb9.geotarget.model.Device;
import ru.cpb9.geotarget.ui.DeviceCone;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import javafx.beans.Observable;
import org.jetbrains.annotations.NotNull;

import javax.swing.SwingUtilities;
import java.util.ArrayList;

/**
 * @author Artem Shein
 * @author Alexander Kuchuk
 */
public class DevicesLayer extends RenderableLayer
{
    @NotNull
    private final DeviceController deviceController;

    private ArrayList<DeviceCone> deviceCones = new ArrayList<>();

    public DevicesLayer(@NotNull String name, @NotNull DeviceController deviceController)
    {
        this.deviceController = deviceController;
        setName(name);

        deviceController.getDeviceRegistry().getDevices().addListener((Observable observable) -> {

//            for (Device device : deviceController.getDeviceRegistry().getDevices())
//            {
//                BasicShapeAttributes shapeAttributes = new BasicShapeAttributes();
//                shapeAttributes.setInteriorMaterial(Material.RED);
//                shapeAttributes.setInteriorOpacity(1.0);
//                shapeAttributes.setEnableLighting(true);
//                shapeAttributes.setOutlineMaterial(Material.RED);
//                shapeAttributes.setOutlineWidth(2.0);
//                shapeAttributes.setDrawInterior(true);
//                shapeAttributes.setDrawOutline(false);
//
//                DeviceCone deviceObject = new DeviceCone(Position.ZERO, Angle.ZERO, Angle.ZERO, Angle.ZERO);
//
//                deviceObject.setAttributes(shapeAttributes);
//                deviceObject.setVisible(false);
//                deviceObject.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
//
//                SwingUtilities.invokeLater(() -> addRenderable(deviceObject));
//
//                deviceCones.add(deviceObject);
//
//                updateDevices(deviceObject, device);
//            }
        });

        deviceController.getWorldWind().getPanel().addMouseWheelListener(e -> {

            double newScale = deviceController.getWorldWind().getPanel().getView().getEyePosition().getAltitude() / 10;

            deviceCones.stream().forEach(deviceCone->deviceCone.setConeSize(newScale));

            e.consume();
        });
    }

    private void updateDevices(DeviceCone deviceObject, Device device)
    {
        /*device.getDevicePositions().addListener((Observable obs) -> {
            deviceObject.setVisible(true);
            SwingUtilities.invokeLater(() -> deviceObjectMove(deviceObject, device));
            SwingUtilities.invokeLater(deviceController::requestRepaint);
        });*/

    }

    private void deviceObjectMove(DeviceCone deviceObject, Device device)
    {
        /*int size = device.getDevicePositions().size() - 1;
        deviceObject.moveTo(device.getDevicePositions().get(size).getPosition());
        deviceObject.setHeading(device.getDevicePositions().get(size).getHeading());
        deviceObject.setRoll(device.getDevicePositions().get(size).getRoll().addDegrees(90));
        deviceObject.setTilt(
                device.getDevicePositions().get(size).getPitch().addDegrees(45));*/
    }

    public ArrayList<DeviceCone> getDeviceCones() {
        return deviceCones;
    }

}