package ru.cpb9.geotarget.ui.layers;

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
import ru.mipt.acsl.geotarget.DeviceController;
import scala.collection.Iterator;

import javax.swing.SwingUtilities;
import java.util.ArrayList;

/**
 * @author Artem Shein
 * @author Alexander Kuchuk
 */
public class DevicesLayer extends RenderableLayer
{
    private static int nextMaterialId = 0;
    @NotNull
    private final DeviceController deviceController;
    @NotNull
    private ArrayList<DeviceCone> deviceCones = new ArrayList<>();

    public DevicesLayer(@NotNull String name, @NotNull DeviceController deviceController)
    {
        this.deviceController = deviceController;
        setName(name);

        deviceController.deviceRegistry().devices().delegate().addListener((Observable observable) -> {

            Iterator<Device> deviceIterator = deviceController.deviceRegistry().devices().toIterator();
            while (deviceIterator.hasNext())
            {
                nextMaterialId = 0;
                BasicShapeAttributes shapeAttributes = new BasicShapeAttributes();
                Material material = getNextMaterial();
                shapeAttributes.setInteriorMaterial(material);
                shapeAttributes.setInteriorOpacity(1.0);
                shapeAttributes.setEnableLighting(true);
                shapeAttributes.setOutlineMaterial(material);
                shapeAttributes.setOutlineWidth(2.0);
                shapeAttributes.setDrawInterior(true);
                shapeAttributes.setDrawOutline(false);

                DeviceCone deviceObject = new DeviceCone(Position.ZERO, Angle.ZERO, Angle.ZERO, Angle.ZERO);

                deviceObject.setAttributes(shapeAttributes);
                deviceObject.setVisible(false);
                deviceObject.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);

                SwingUtilities.invokeLater(() -> addRenderable(deviceObject));

                deviceCones.add(deviceObject);

                updateDevicePosition(deviceObject, deviceIterator.next());
            }
        });

        deviceController.worldWind().getPanel().addMouseWheelListener(e -> {

            double newScale = deviceController.worldWind().getPanel().getView().getEyePosition().getAltitude() / 10;

            deviceCones.stream().forEach(deviceCone->deviceCone.setConeSize(newScale));

            e.consume();
        });
    }

    @NotNull
    private static Material getNextMaterial()
    {
        switch (nextMaterialId++ % 8)
        {
            case 0:
                return Material.BLUE;
            case 1:
                return Material.GREEN;
            case 2:
                return Material.RED;
            case 3:
                return Material.YELLOW;
            case 4:
                return Material.CYAN;
            case 5:
                return Material.GRAY;
            case 6:
                return Material.MAGENTA;
            case 7:
                return Material.ORANGE;
        }
        throw new AssertionError();
    }

    private void updateDevicePosition(@NotNull DeviceCone deviceObject, @NotNull Device device)
    {
        device.getDevicePositions().addListener((Observable obs) -> {
            deviceObject.setVisible(true);
            SwingUtilities.invokeLater(() -> moveDeviceObject(deviceObject, device));
            SwingUtilities.invokeLater(deviceController::requestRepaint);
        });
    }

    private void moveDeviceObject(@NotNull DeviceCone deviceObject, @NotNull Device device)
    {
        int size = device.getDevicePositions().size() - 1;
        deviceObject.moveTo(device.getDevicePositions().get(size).getPosition());
        deviceObject.setHeading(device.getDevicePositions().get(size).getHeading());
        deviceObject.setRoll(device.getDevicePositions().get(size).getRoll().addDegrees(90.));
        deviceObject.setTilt(
                device.getDevicePositions().get(size).getPitch().addDegrees(135));
    }

    @NotNull
    public ArrayList<DeviceCone> getDeviceCones() {
        return deviceCones;
    }

}