package ru.cpb9.geotarget.ui;

import c10n.C10N;
import com.google.common.io.Resources;
import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WWObject;
import gov.nasa.worldwind.WWObjectImpl;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.event.Message;
import gov.nasa.worldwind.geom.Extent;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.ElevationModel;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.layers.Earth.BMNGOneImage;
import gov.nasa.worldwind.terrain.BasicElevationModel;
import gov.nasa.worldwind.terrain.CompoundElevationModel;
import gov.nasa.worldwind.wms.WMSTiledImageLayer;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.cpb9.geotarget.GeoTargetException;
import ru.cpb9.geotarget.ui.layers.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @author Artem Shein
 */
public class GeoTargetModel extends WWObjectImpl implements Model
{
    private static final Messages I = C10N.get(Messages.class);
    private final ViewControlsLayer controlsLayer;
    private Globe globe;
    private LayerList layers;
    private boolean showWireframeInterior = false;
    private boolean showWireframeExterior = false;
    private boolean showTessellationBoundingVolumes = false;

    public GeoTargetModel()
    {
        setGlobe(new Earth());
        controlsLayer = withName(new ViewControlsLayer(), I.controls());
        setLayers(new LayerList(
                new Layer[]{withName(new StarsLayer(), I.stars()), withName(new SkyGradientLayer(), I.skyGradient()),
                        disabled(withName(new BlueSphereLayer(), I.blueSphere())),
                        withName(new BMNGOneImage(), I.fullSizeEarthImage()),
                        makeWmsTiledLayer("config/Earth/BingImagery.xml"),
                        makeWmsTiledLayer("config/Earth/BMNG256.xml"),
                        makeWmsTiledLayer("config/Earth/BMNGWMSLayer.xml"),
                        makeWmsTiledLayer("config/Earth/BMNGWMSLayer2.xml"),
                        makeWmsTiledLayer("config/Earth/USGSTopoLowResLayer.xml"),
                        makeWmsTiledLayer("config/Earth/USGSTopoMedResLayer.xml"),
                        makeWmsTiledLayer("config/Earth/USGSTopoHighResLayer.xml"),
                        makeWmsTiledLayer("config/Earth/USGSUrbanAreaOrthoLayer.xml"),
                        makeWmsTiledLayer("config/Earth/USGSDigitalOrthoLayer.xml"),
                        makeWmsTiledLayer("config/Earth/USDANAIPWMSImageLayer.xml"),
                        makeWmsTiledLayer("config/Earth/USDANAIPUSGSWMSImageLayer.xml"),
                        makeWmsTiledLayer("config/Earth/ScankortDenmarkImageLayer.xml"),
                        makeWmsTiledLayer("config/Earth/Landsat256.xml"),
                        makeWmsTiledLayer("config/Earth/LandsatI3WMSLayer.xml"),
                        makeWmsTiledLayer("config/Earth/LandsatI3WMSLayer2.xml"),
                        makeWmsTiledLayer("config/Earth/MSVirtualEarthAerialLayer.xml"),
                        makeWmsTiledLayer("config/Earth/MSVirtualEarthHybridLayer.xml"),
                        makeWmsTiledLayer("config/Earth/MSVirtualEarthRoadsLayer.xml"),
                        makeWmsTiledLayer("config/Earth/EarthAtNightLayer.xml"),
                        makeWmsTiledLayer("config/Earth/CountryBoundariesLayer.xml"),
                        makeWmsTiledLayer("config/Earth/AlaskaFAASectionals.xml"),
                        disabled(new GoogleSatelliteLayer(I.googleSatellite())),
                        disabled(new GoogleMapLayer(I.googleMaps())),
                        disabled(new GoogleLandscapeLayer(I.googleLandscape())),
                        disabled(new YandexMapLayer(I.yandexMaps())),
                        new YandexSatelliteLayer(I.yandexSatellite()),
                        disabled(new YandexNarodnayaLayer(I.yandexNarodnaya())),
                        disabled(
                                withName(makeWmsTiledLayer("config/Earth/OpenStreetMap.xml"), I.openStreetMap())),
                        controlsLayer,
                        withName(new CompassLayer(), I.compass()),
                        withName(new ScalebarLayer(), I.scaleBar()), withName(new WorldMapLayer(), I.worldMap())}));
    }

    private static <T extends Layer> T withName(@NotNull T layer, @NotNull String name)
    {
        layer.setName(name);
        return layer;
    }

    @NotNull
    private static <T extends Layer> T disabled(@NotNull T layer)
    {
        layer.setEnabled(false);
        return layer;
    }

    private ElevationModel makeElevationModel(String resourceName)
    {
        ElevationModel elevationModel;
        try
        {
            Document document = parseResourceXml(resourceName);
            String modelType = document.getDocumentElement().getAttribute("modelType");
            if (modelType.equals("Compound"))
            {
                CompoundElevationModel compoundElevationModel = new CompoundElevationModel();
                NodeList childNodes = document.getDocumentElement().getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++)
                {

                    Node node = childNodes.item(i);
                    if (node instanceof Element)
                    {
                        compoundElevationModel.getElevationModels().add(new BasicElevationModel((Element) node, null));
                    }
                }
                elevationModel = compoundElevationModel;
            }
            else
            {
                elevationModel = new BasicElevationModel(document, null);
            }
        }
        catch (SAXException | IOException | ParserConfigurationException | IllegalArgumentException e)
        {
            throw new GeoTargetException(resourceName, e);
        }
        return elevationModel;
    }

    private TiledImageLayer makeWmsTiledLayer(String resourceName)
    {
        TiledImageLayer imageLayer;
        try
        {
            Document document = parseResourceXml(resourceName);
            String layerType = document.getDocumentElement().getAttribute("layerType");
            switch (layerType)
            {
                case "TiledImageLayer":
                    imageLayer = new WMSTiledImageLayer(document, null);
                    break;
                default:
                    throw new GeoTargetException(
                            String.format("unsupported layer type '%s' in resource '%s'", layerType, resourceName));

            }
        }
        catch (SAXException | IOException | ParserConfigurationException | IllegalArgumentException e)
        {
            throw new GeoTargetException(resourceName, e);
        }
        imageLayer.setEnabled(false);
        return imageLayer;
    }

    private Document parseResourceXml(String resourceName) throws SAXException, IOException,
            ParserConfigurationException
    {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                Resources.getResource(resourceName).toExternalForm());
    }

    @Override
    public Extent getExtent()
    {
        return getGlobe().getExtent();
    }

    @Override
    public Globe getGlobe()
    {
        return globe;
    }

    @Override
    public LayerList getLayers()
    {
        return layers;
    }

    @Override
    public void setGlobe(Globe globe)
    {
        this.globe = updatePropertyChangeListeners(this.globe, globe);
    }

    private <T extends WWObject> T updatePropertyChangeListeners(T oldValue, T newValue)
    {
        if (oldValue != null)
        {
            oldValue.removePropertyChangeListener(this);
        }
        newValue.addPropertyChangeListener(this);
        return newValue;
    }

    @Override
    public void setLayers(LayerList layerList)
    {
        this.layers = updatePropertyChangeListeners(this.layers, layerList);
    }

    @Override
    public void setShowWireframeInterior(boolean b)
    {
        this.showWireframeInterior = b;
    }

    @Override
    public void setShowWireframeExterior(boolean b)
    {
        this.showWireframeExterior = b;
    }

    @Override
    public boolean isShowWireframeInterior()
    {
        return showWireframeInterior;
    }

    @Override
    public boolean isShowWireframeExterior()
    {
        return showWireframeExterior;
    }

    @Override
    public boolean isShowTessellationBoundingVolumes()
    {
        return showTessellationBoundingVolumes;
    }

    @Override
    public void setShowTessellationBoundingVolumes(boolean b)
    {
        this.showTessellationBoundingVolumes = b;
    }

    @Override
    public void onMessage(Message message)
    {
        for (Layer layer : layers)
        {
            layer.onMessage(message);
        }
    }

    public void initialize(@NotNull WorldWindow wwPanel)
    {
        ViewControlsSelectListener viewControlsSelectListener = new ViewControlsSelectListener(wwPanel, controlsLayer);
        wwPanel.addSelectListener(viewControlsSelectListener::selected);
    }
}
