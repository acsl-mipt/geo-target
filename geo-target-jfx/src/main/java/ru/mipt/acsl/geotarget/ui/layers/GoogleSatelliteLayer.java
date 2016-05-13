package ru.mipt.acsl.geotarget.ui.layers;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * @author Artem Shein
 */
public class GoogleSatelliteLayer extends OnlineServiceMerkatorLayer
{
    private static final int MAX_ZOOM_LEVEL = 20;
    private static final int MIN_ZOOM_LEVEL = 0;
    private static final String FORMAT_SUFFIX = ".jpg";

    public GoogleSatelliteLayer(@NotNull String name)
    {
        super(name, "GoogleSatellite", MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL, (tile, imageFormat) -> {
            int server = (int)(Math.random() * 4);
            int gagarin = (int)(Math.random() * 6) + 1;
            int x = tile.getColumn();
            int y = numColumnsInLevel(tile.getLevel()) - tile.getRow() - 1;
            return new URL("http://khms" + server + ".google.com/kh/v=161&src=app&s=Galileo" + gagarin + "&x=" + x
                    + "&y=" + y + "&z=" + tile.getLevelNumber());
        }, FORMAT_SUFFIX);
    }




}
