package ru.cpb9.geotarget;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import c10n.C10N;
import c10n.annotations.DefaultC10NAnnotations;
import com.google.common.io.Resources;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import ru.cpb9.geotarget.ui.AddDeviceWidget;
import ru.cpb9.geotarget.ui.Widget;
import ru.cpb9.geotarget.ui.controls.parameters.tree.ParametersTree;
import ru.cpb9.geotarget.client.akka.ActorName;
import ru.cpb9.geotarget.client.akka.PositionOrientationUpdateActor;
import ru.cpb9.geotarget.server.db.LocalDbServerActor;
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
    public static final String ACTOR_SYSTEM_NAME = "GeoTargetAkka";

    private static final Logger LOG = LoggerFactory.getLogger(GeoTargetApplication.class);
    private static Messages I;

    public final ActorSystem actorSystem = ActorSystem.create(ACTOR_SYSTEM_NAME);

    public final DeviceController deviceController = new SimpleDeviceController(SimpleDeviceRegistry.newInstance(), new WorldWindNode(new GeoTargetModel()));
    private final Set<ActorRef> actors = new HashSet<>();

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

        ActorRef localDb = makeActorRef(LocalDbServerActor.class, ActorName.LOCAL_DB_SERVER, Resources.getResource(
                "ru/cpb9/ifdev/local.sqlite"));
        makeActorRef(PositionOrientationUpdateActor.class, ActorName.POSITION_UPDATE_ACTOR, deviceController.getDeviceRegistry());

        Widget addDeviceWidget = new AddDeviceWidget(deviceController);
        Widget parametersTableWidget = new Widget("Parameters table", new ParametersTable(deviceController));
        Widget parametersTreeWidget = new Widget("Parameters tree", new ParametersTree(deviceController));
        Widget deviceListWidget = new Widget("Device list", new DeviceList(deviceController));
        Widget artificialHorizonWidget =
                new Widget("Artificial horizon", new ArtificialHorizonPane(deviceController.getDeviceRegistry()));
        WorldWindNode worldWindNode = deviceController.getWorldWind();
        Widget layerListWidget = new Widget("Layer list", new LayersList(worldWindNode));

        MenuBar mainMenu = new MenuBar();
        Menu fileMenu = new Menu(I.file());
        MenuItem exitMenuItem = new MenuItem(I.exit());
        exitMenuItem.setOnAction(e -> requestShutdown());
        fileMenu.getItems().addAll(exitMenuItem);

        Menu viewMenu = new Menu(I.view());
        CheckMenuItem parametersTableWidgetMenuItem = new CheckMenuItem(I.parametersTable());
        parametersTableWidget.visibleProperty().bind(parametersTableWidgetMenuItem.selectedProperty());
        viewMenu.getItems().addAll(
                newBindedToWidgetVisibilityCheckMenuItem(I.addDevice(), addDeviceWidget),
                newBindedToWidgetVisibilityCheckMenuItem(I.parametersTable(), parametersTableWidget),
                newBindedToWidgetVisibilityCheckMenuItem(I.parametersTree(), parametersTreeWidget),
                newBindedToWidgetVisibilityCheckMenuItem(I.deviceList(), deviceListWidget),
                newBindedToWidgetVisibilityCheckMenuItem(I.artificialHorizon(), artificialHorizonWidget),
                newBindedToWidgetVisibilityCheckMenuItem(I.layerList(), layerListWidget));

        mainMenu.getMenus().addAll(fileMenu, viewMenu);

        Pane mainPane = new Pane();
        mainPane.getChildren().addAll(worldWindNode, addDeviceWidget,
                parametersTableWidget,
                parametersTreeWidget,
                deviceListWidget,
                artificialHorizonWidget,
                layerListWidget);

        VBox vBox = new VBox(mainMenu, mainPane);
        VBox.setVgrow(mainPane, Priority.ALWAYS);
        primaryStage.setScene(new Scene(vBox, 1200, 1000));
        primaryStage.setTitle(I.appName());
        primaryStage.show();

    }

    @NotNull
    private static CheckMenuItem newBindedToWidgetVisibilityCheckMenuItem(@NotNull String text, @NotNull Widget widget)
    {
        CheckMenuItem menuItem = new CheckMenuItem(text);
        widget.visibleProperty().bind(menuItem.selectedProperty());
        widget.getCloseButton().setOnAction((e) -> menuItem.setSelected(false));
        return menuItem;
    }

    @NotNull
    private ActorRef makeActorRef(@NotNull Class<? extends UntypedActor> actorClass, @NotNull ActorName actorName, @NotNull Object... parameters)
    {
        return registerActorRef(actorSystem.actorOf(Props.create(actorClass, parameters), actorName.getName()));
    }

    @NotNull
    private ActorRef registerActorRef(@NotNull ActorRef actorRef)
    {
        actors.add(actorRef);
        return actorRef;
    }

    public void requestShutdown()
    {
        LOG.info("Shutdown requested, exiting");
        try
        {
            actors.forEach(actorSystem::stop);
            actorSystem.shutdown();
        }
        finally
        {
            Platform.exit();
        }
    }
}
