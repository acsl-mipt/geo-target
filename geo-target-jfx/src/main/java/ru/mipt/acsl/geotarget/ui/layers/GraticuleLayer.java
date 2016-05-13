package ru.mipt.acsl.geotarget.ui.layers;

import gov.nasa.worldwind.layers.LatLonGraticuleLayer;
import org.jetbrains.annotations.NotNull;

/**
 * @author Alexander Kuchuk.
 */


public class GraticuleLayer extends LatLonGraticuleLayer
{
    public GraticuleLayer(@NotNull String name)
    {
        setName(name);
        setPickEnabled(true);
        setEnabled(false);
    }
}
