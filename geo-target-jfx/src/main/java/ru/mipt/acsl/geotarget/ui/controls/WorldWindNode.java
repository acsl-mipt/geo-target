package ru.mipt.acsl.geotarget.ui.controls;

import ru.mipt.acsl.geotarget.GeoTargetException;
import ru.mipt.acsl.geotarget.ui.GeoTargetModel;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import javafx.embed.swing.SwingNode;

import javax.swing.*;
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
        }
        catch (InterruptedException | ExecutionException e)
        {
            throw new GeoTargetException(e);
        }
    }

    @Override
    public boolean isResizable()
    {
        return false;
    }

    public WorldWindowGLJPanel getPanel()
    {
        return wwPanel;
    }
}
