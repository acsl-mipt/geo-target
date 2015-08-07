package ru.cpb9.geotarget;

import akka.actor.ActorRef;
import c10n.C10N;
import c10n.annotations.DefaultC10NAnnotations;
import javafx.beans.InvalidationListener;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import ru.cpb9.geotarget.akka.server.TmServerActor;
import ru.cpb9.geotarget.ui.AddDeviceWidget;
import ru.cpb9.geotarget.ui.Widget;
import ru.cpb9.geotarget.ui.controls.parameters.tree.ParametersTree;
import ru.cpb9.geotarget.client.akka.ActorName;
import ru.cpb9.geotarget.client.akka.PositionOrientationUpdateActor;
import ru.cpb9.geotarget.ui.GeoTargetModel;
import ru.cpb9.geotarget.ui.LayersList;
import ru.cpb9.geotarget.ui.controls.DeviceList;
import ru.cpb9.geotarget.ui.controls.WorldWindNode;
import ru.cpb9.geotarget.ui.controls.parameters.flightdevice.ArtificialHorizonPane;
import ru.cpb9.geotarget.ui.controls.parameters.table.ParametersTable;
import ru.cpb9.geotarget.ui.layers.DeviceTailsLayer;
import ru.cpb9.geotarget.ui.layers.DevicesLayer;
import ru.cpb9.geotarget.ui.layers.GraticuleLayer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Artem Shein
 */
public class GeoTargetApplication extends Application
{


    private static final Logger LOG = LoggerFactory.getLogger(GeoTargetApplication.class);
    private static Messages I;

    private static final ActorsRegistry ACTORS_REGISTRY = ActorsRegistry.getInstance();

    public final DeviceController deviceController = new SimpleDeviceController(SimpleDeviceRegistry.newInstance(), new WorldWindNode(new GeoTargetModel()));

    static public void main(String[] args)
    {
        try
        {
            C10N.configure(new DefaultC10NAnnotations());
            Locale.setDefault(new Locale("ru"));
            I = C10N.get(Messages.class);
            launch(args);
        }
        catch (Throwable e)
        {
            LOG.error(ExceptionUtils.getStackTrace(e));
            System.exit(1);
        }
        System.exit(0);
    }

    @Override
    public void start(@NotNull Stage primaryStage) throws Exception
    {
        LOG.info("Starting...");
        List<String> parameters = getParameters().getUnnamed();

        deviceController.getWorldWind().getPanel().getModel().getLayers().add(new DevicesLayer(I.devices(), deviceController));
        deviceController.getWorldWind().getPanel().getModel().getLayers().add(
                new DeviceTailsLayer(I.deviceTail(), deviceController));
        deviceController.getWorldWind().getPanel().getModel().getLayers().add(new GraticuleLayer(I.graticule()));

        for (Controller controller : ControllerEnvironment.getDefaultEnvironment().getControllers())
        {
        }

        ACTORS_REGISTRY.makeActor(PositionOrientationUpdateActor.class, ActorName.POSITION_UPDATE_ACTOR.getName(),
                deviceController.getDeviceRegistry());
        ActorRef tmServerActorRef = ACTORS_REGISTRY.makeActor(TmServerActor.class, ActorName.TM_SERVER.getName());

        Widget addDeviceWidget = new AddDeviceWidget(deviceController, tmServerActorRef);
        Widget parametersTableWidget = new Widget(I.parametersTable(), new ParametersTable(deviceController));
        Widget parametersTreeWidget = new Widget(I.parametersTree(), new ParametersTree(deviceController));
        Widget deviceListWidget = new Widget(I.deviceList(), new DeviceList(deviceController));
        Widget artificialHorizonWidget =
                new Widget(I.artificialHorizon(), new ArtificialHorizonPane(deviceController.getDeviceRegistry()));
        WorldWindNode worldWindNode = deviceController.getWorldWind();
        Widget layerListWidget = new Widget(I.layerList(), new LayersList(worldWindNode));

        MenuBar mainMenu = new MenuBar();
        Menu fileMenu = new Menu(I.file());
        MenuItem exitMenuItem = new MenuItem(I.exit());
        exitMenuItem.setOnAction(e -> requestShutdown());
        fileMenu.getItems().addAll(exitMenuItem);

        Menu viewMenu = new Menu(I.view());
        CheckMenuItem parametersTableWidgetMenuItem = new CheckMenuItem(I.parametersTable());
        parametersTableWidget.visibleProperty().bind(parametersTableWidgetMenuItem.selectedProperty());
        viewMenu.getItems().addAll(
                newBindedToWidgetVisibilityCheckMenuItem(addDeviceWidget),
                newBindedToWidgetVisibilityCheckMenuItem(parametersTableWidget),
                newBindedToWidgetVisibilityCheckMenuItem(parametersTreeWidget),
                newBindedToWidgetVisibilityCheckMenuItem(deviceListWidget),
                newBindedToWidgetVisibilityCheckMenuItem(artificialHorizonWidget),
                newBindedToWidgetVisibilityCheckMenuItem(layerListWidget));

        mainMenu.getMenus().addAll(fileMenu, viewMenu);

        Pane mainPane = new Pane();
        mainPane.getChildren().addAll(worldWindNode, addDeviceWidget,
                parametersTableWidget,
                parametersTreeWidget,
                deviceListWidget,
                artificialHorizonWidget,
                layerListWidget);

        InvalidationListener sizeUpdater = (e) -> {
            worldWindNode.resize(mainPane.getWidth(), mainPane.getHeight());
            worldWindNode.getPanel().setSize((int) mainPane.getWidth(), (int) mainPane.getHeight());
        };
        mainPane.heightProperty().addListener(sizeUpdater);
        mainPane.widthProperty().addListener(sizeUpdater);

        VBox vBox = new VBox(mainMenu, mainPane);
        VBox.setVgrow(mainPane, Priority.ALWAYS);
        primaryStage.setScene(new Scene(vBox, 1200, 1000));
        primaryStage.setTitle(I.appName());
        primaryStage.show();

    }

    @NotNull
    private static CheckMenuItem newBindedToWidgetVisibilityCheckMenuItem(@NotNull Widget widget)
    {
        CheckMenuItem menuItem = new CheckMenuItem(widget.getTitle());
        widget.visibleProperty().bind(menuItem.selectedProperty());
        widget.getCloseButton().setOnAction((e) -> menuItem.setSelected(false));
        return menuItem;
    }

    public void requestShutdown()
    {
        LOG.info("Shutdown requested, exiting");
        try
        {
            ACTORS_REGISTRY.shutdown();
        }
        finally
        {
            Platform.exit();
        }
    }
}
