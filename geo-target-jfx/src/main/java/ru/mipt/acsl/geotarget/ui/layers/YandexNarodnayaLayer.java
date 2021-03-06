package ru.mipt.acsl.geotarget.ui.layers;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

/**
 * @author Artem Shein
 */
public class YandexNarodnayaLayer extends OnlineServiceMerkatorLayer
{
    private static final int MIN_ZOOM_LEVEL = 0;
    private static final int MAX_ZOOM_LEVEL = 18;
    private static final String FORMAT_SUFFIX = ".png";

    public YandexNarodnayaLayer(@NotNull String name)
    {
        super(name, "YandexMaps", MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL, (tile, imageFormat) -> {
            int server = (int)(Math.random() * 4);
            int randomKey = (int)(Math.random() * 6) + 1;
            int x = tile.getColumn();
            int y = OnlineServiceMerkatorLayer.numColumnsInLevel(tile.getLevel()) - tile.getRow() - 1;
            return new URL("http://0" + server + ".pvec.maps.yandex.net/?l=pmap&lang=ru-RU&" + randomKey + "&x=" + x
                    + "&y=" + y + "&z=" + tile.getLevelNumber());
        }, FORMAT_SUFFIX);
    }
}
