package ru.mipt.acsl.geotarget.ui.layers;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * @author Artem Shein
 */
public class GoogleLandscapeLayer extends OnlineServiceMerkatorLayer
{
    private static final int MAX_ZOOM_LEVEL = 23;
    private static final int MIN_ZOOM_LEVEL = 0;
    private static final String FORMAT_SUFFIX = ".png";

    public GoogleLandscapeLayer(@NotNull String name)
    {
        super(name, "GoogleLandscape", MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL, (tile, imageFormat) -> {
            int server = (int)(Math.random() * 4);
            int randomKey = (int)(Math.random() * 6) + 1;
            int x = tile.getColumn();
            int y = numColumnsInLevel(tile.getLevel()) - tile.getRow() - 1;
            return new URL("http://mt" + server + ".google.com/vt/lyrs=t@130,r@206000000&hl=ru" + randomKey + "&x=" + x
                    + "&y=" + y + "&z=" + tile.getLevelNumber());
        }, FORMAT_SUFFIX);
    }
}
