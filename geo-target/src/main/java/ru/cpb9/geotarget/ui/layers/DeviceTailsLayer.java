package ru.cpb9.geotarget.ui.layers;

import ru.cpb9.device.modeling.flying.PositionOrientation;
import ru.cpb9.geotarget.model.Device;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.ShapeAttributes;
import javafx.beans.Observable;
import org.jetbrains.annotations.NotNull;
import ru.mipt.acsl.geotarget.DeviceController;
import scala.collection.Iterator;

import javax.swing.*;
import java.util.stream.Collectors;

/**
 * @author Alexander Kuchuk
 */

public class DeviceTailsLayer extends RenderableLayer
{
    public DeviceTailsLayer(@NotNull String name, @NotNull DeviceController deviceController)
    {
        setName(name);
        setPickEnabled(true);

        deviceController.deviceRegistry().devices().delegate().addListener((Observable observable) -> {
            Iterator<Device> it = deviceController.deviceRegistry().devices().toIterator();
            while (it.hasNext())
            {
                ShapeAttributes attrs = new BasicShapeAttributes();
                attrs.setOutlineMaterial(Material.RED);
                attrs.setOutlineWidth(2d);

                Path deviceTail = new Path();
                deviceTail.setAttributes(attrs);
                deviceTail.setVisible(true);
                deviceTail.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);

                SwingUtilities.invokeLater(() -> addRenderable(deviceTail));
                updateLine(deviceTail, it.next());
            }
        });

    }

    private void updateLine(Path deviceTail, Device device)
    {
        device.getDevicePositions().addListener((Observable obs)->{
            SwingUtilities.invokeLater(() -> deviceTail.setPositions(
                    device.getDevicePositions().stream().map(PositionOrientation::getPosition)
                            .collect(Collectors.toList())));
        });

    }

}