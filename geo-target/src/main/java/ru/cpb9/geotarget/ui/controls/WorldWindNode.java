package ru.cpb9.geotarget.ui.controls;

import ru.cpb9.geotarget.GeoTargetException;
import ru.cpb9.geotarget.ui.GeoTargetModel;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import javafx.embed.swing.SwingNode;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Artem Shein
 */
public class WorldWindNode extends SwingNode
{
    private WorldWindowGLJPanel wwPanel;

    public WorldWindNode(GeoTargetModel model)
    {
        final FutureTask<JPanel> awtInitTask = new FutureTask<>(() -> {
            wwPanel = new WorldWindowGLJPanel();
            wwPanel.setVisible(true);
            model.initialize(wwPanel);
            wwPanel.setModel(model);
            return wwPanel;
        });

        SwingUtilities.invokeLater(awtInitTask);
        try
        {
            JPanel content = awtInitTask.get();
            setContent(content);
            content.setMaximumSize();
        }
        catch (InterruptedException | ExecutionException e)
        {
            throw new GeoTargetException(e);
        }
    }

    public WorldWindowGLJPanel getPanel()
    {
        return wwPanel;
    }
}
