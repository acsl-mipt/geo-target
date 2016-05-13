package ru.mipt.acsl.geotarget;

import akka.actor.ActorRef;
import c10n.C10N;
import c10n.annotations.DefaultC10NAnnotations;
import com.google.common.collect.Lists;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.mipt.acsl.geotarget.akka.ActorName;
import ru.mipt.acsl.geotarget.akka.server.TmServerActor;
import ru.mipt.acsl.geotarget.ui.AddDeviceWidget;
import ru.mipt.acsl.geotarget.ui.GeoTargetModel;
import ru.mipt.acsl.geotarget.ui.LayersList;
import ru.mipt.acsl.geotarget.ui.Widget;
import ru.mipt.acsl.geotarget.ui.controls.DeviceList;
import ru.mipt.acsl.geotarget.ui.controls.WorldWindNode;
import ru.mipt.acsl.geotarget.ui.controls.parameters.flightdevice.ArtificialHorizonPane;
import ru.mipt.acsl.geotarget.ui.controls.parameters.tree.ParametersTree;
import ru.mipt.acsl.geotarget.ui.layers.DeviceTailsLayer;
import ru.mipt.acsl.geotarget.ui.layers.DevicesLayer;
import ru.mipt.acsl.geotarget.ui.layers.GraticuleLayer;

import java.util.Locale;

/**
 * @author Artem Shein
 */
public class GeoTargetJfxApplication extends Application
{
    static
    {
        C10N.configure(new DefaultC10NAnnotations());
        Locale.setDefault(new Locale("ru"));
    }

    private static final Messages I = C10N.get(Messages.class);
    private static final ActorsRegistry ACTORS_REGISTRY = ActorsRegistry.getInstance();
    private static final ActorRef tmServerActorRef = ACTORS_REGISTRY.makeActor(TmServerActor.class,
            ActorName.TM_SERVER.getName());

    private final DeviceController deviceController =
            DeviceController.newInstance(DeviceRegistry.newInstance(tmServerActorRef),
                    new WorldWindNode(new GeoTargetModel()));

    public GeoTargetJfxApplication() {
        deviceController.getWorldWind().getPanel().getModel().getLayers().addAll(Lists.newArrayList(
                new DevicesLayer(I.devices(), deviceController),
                new DeviceTailsLayer(I.deviceTail(), deviceController),
                new GraticuleLayer(I.graticule())));
    }

    private final AddDeviceWidget addDeviceWidget = new AddDeviceWidget(deviceController, tmServerActorRef);
    private final Widget parametersTreeWidget = new Widget(I.parametersTree(), new ParametersTree(deviceController));
    private final Widget deviceListWidget =
            new Widget(I.deviceList(), new DeviceList(deviceController, tmServerActorRef));
    private final Widget artificialHorizonWidget = new Widget(I.artificialHorizon(),
            new ArtificialHorizonPane(deviceController.getDeviceRegistry()));
    private final WorldWindNode worldWindNode = deviceController.getWorldWind();
    private final Widget layerListWidget = new Widget(I.layerList(), new LayersList(worldWindNode));

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Menu fileMenu = new Menu(I.file());
        MenuItem exitItem = new MenuItem(I.exit());
        exitItem.setOnAction(ae -> requestShutdown());
        fileMenu.getItems().addAll(exitItem);
        Menu viewMenu = new Menu(I.view());
        viewMenu.getItems().addAll(
                newBindedToWidgetVisibilityCheckMenuItem(addDeviceWidget),
                newBindedToWidgetVisibilityCheckMenuItem(parametersTreeWidget),
                newBindedToWidgetVisibilityCheckMenuItem(deviceListWidget),
                newBindedToWidgetVisibilityCheckMenuItem(artificialHorizonWidget),
                newBindedToWidgetVisibilityCheckMenuItem(layerListWidget));

        MenuBar mainMenu = new MenuBar();
        mainMenu.getMenus().addAll(fileMenu, viewMenu);

        Pane mainPane = new Pane();
        mainPane.getChildren().addAll(worldWindNode, addDeviceWidget, parametersTreeWidget, deviceListWidget,
                artificialHorizonWidget, layerListWidget);

        InvalidationListener sizeUpdater = (Observable o) -> {
            worldWindNode.resize(mainPane.getWidth(), mainPane.getHeight());
            worldWindNode.getPanel().setSize((int) mainPane.getWidth(), (int) mainPane.getHeight());
        };

        mainPane.heightProperty().addListener(sizeUpdater);
        mainPane.widthProperty().addListener(sizeUpdater);

        VBox vBox = new VBox(mainMenu, mainPane);
        VBox.setVgrow(mainPane, Priority.ALWAYS);
        primaryStage.setScene(new Scene(vBox, 1200, 1000));
        primaryStage.setTitle(I.appName());
        primaryStage.setOnCloseRequest(e -> requestShutdown());

        Runtime.getRuntime().addShutdownHook(new Thread(this::tryShutdownGracefully));
        primaryStage.show();
    }


    private CheckMenuItem newBindedToWidgetVisibilityCheckMenuItem(Widget widget)
    {
        CheckMenuItem menuItem = new CheckMenuItem(widget.getTitle());
        widget.visibleProperty().bind(menuItem.selectedProperty());
        widget.getCloseButton().setOnAction(e -> menuItem.setSelected(false));
        return menuItem;
    }

    private void tryShutdownGracefully()
    {
        try
        {
            ACTORS_REGISTRY.shutdown();
        }
        finally
        {
            Platform.exit();
        }
    }


    //fixme addhoc decision with System.exit
    private void requestShutdown()
    {
        tryShutdownGracefully();
        System.exit(0);
    }


}
