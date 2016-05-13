package ru.mipt.acsl.geotarget.ui;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Cone;

import javax.swing.*;

/**
 * @author Alexander Kuchuk.
 */


public class DeviceCone extends Cone
{
    private static double SCALE = 1907000;

    public DeviceCone(Position position, Angle heading, Angle roll, Angle pitch)
    {
        super(position, SCALE / 40, SCALE / 5, SCALE / 10, heading, roll, pitch);
    }

    public void setConeSize(double newScale)
    {
        SwingUtilities.invokeLater(() ->{
            setEastWestRadius(newScale / 10);
            setNorthSouthRadius(newScale / 40);
            setVerticalRadius(newScale / 5);
        });
    }
}
