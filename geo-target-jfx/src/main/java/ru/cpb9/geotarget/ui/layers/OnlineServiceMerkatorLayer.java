package ru.cpb9.geotarget.ui.layers;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.avlist.AVListImpl;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.layers.mercator.BasicMercatorTiledImageLayer;
import gov.nasa.worldwind.layers.mercator.MercatorSector;
import gov.nasa.worldwind.util.Level;
import gov.nasa.worldwind.util.LevelSet;
import gov.nasa.worldwind.util.TileUrlBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public abstract class OnlineServiceMerkatorLayer extends BasicMercatorTiledImageLayer
{
    public OnlineServiceMerkatorLayer(@NotNull String name, @NotNull String dataSetName, int minZoomLevel,
                                      int maxZoomLevel, @NotNull TileUrlBuilder tileUrlBuilder,
                                      @NotNull String formatSuffix)
    {
        super(new LevelSet(makeConfig(dataSetName, minZoomLevel, maxZoomLevel, tileUrlBuilder, formatSuffix)));
        setName(name);
    }

    @NotNull
    private static AVList makeConfig(@NotNull String dataSetName, int minZoomLevel, int maxZoomLevel, @NotNull TileUrlBuilder tileUrlBuilder, @NotNull String formatSuffix)
    {
        AVList config = new AVListImpl();
        config.setValue(AVKey.LEVEL_ZERO_TILE_DELTA, new LatLon(Angle.POS180, Angle.POS360));
        config.setValue(AVKey.SECTOR, MercatorSector.fromSector(Sector.FULL_SPHERE));
        config.setValue(AVKey.NUM_LEVELS, maxZoomLevel - minZoomLevel);
        config.setValue(AVKey.TILE_WIDTH, 256);
        config.setValue(AVKey.TILE_HEIGHT, 256);
        config.setValue(AVKey.DATA_CACHE_NAME, dataSetName);
        config.setValue(AVKey.DATASET_NAME, dataSetName);
        config.setValue(AVKey.FORMAT_SUFFIX, formatSuffix);
        config.setValue(AVKey.TILE_URL_BUILDER, tileUrlBuilder);
        return config;
    }

    protected static int numColumnsInLevel(@NotNull Level level)
    {
        int levelDelta = level.getLevelNumber();
        double twoToTheN = Math.pow(2, levelDelta);
        return (int) (twoToTheN * 1);
    }
}
