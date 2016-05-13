package ru.mipt.acsl.geotarget.ui.layers;

import com.sun.javafx.geom.Vec3f;
import ru.mipt.acsl.geotarget.ui.ColoredSphere;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;

/**
 * @author Artem Shein
 */
public class BlueSphereLayer extends RenderableLayer
{

    public static final double EARTH_RADIUS = 6_371_000.0;

    public BlueSphereLayer()
    {
        addRenderable(ColoredSphere.newInstance(Position.ZERO, EARTH_RADIUS - 1, new Vec3f(0.3f, 0.5f, 1f)));
    }
}
