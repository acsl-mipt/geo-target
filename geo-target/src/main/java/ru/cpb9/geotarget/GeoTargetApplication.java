package ru.cpb9.geotarget;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import c10n.C10N;
import c10n.annotations.DefaultC10NAnnotations;
import com.google.common.io.Resources;
import ru.cpb9.geotarget.exchange.MavlinkDevice;
import ru.cpb9.geotarget.ui.controls.parameters.tree.ParametersTree;
import ru.cpb9.geotarget.client.akka.ActorName;
import ru.cpb9.geotarget.client.akka.PositionOrientationUpdateActor;
import ru.cpb9.geotarget.server.db.LocalDbServerActor;
import ru.cpb9.geotarget.ui.GeoTargetModel;
import ru.cpb9.geotarget.ui.LayersList;
import ru.cpb9.geotarget.ui.controls.DeviceList;
import ru.cpb9.geotarget.ui.controls.WorldWindNode;
import ru.cpb9.geotarget.ui.controls.parameters.flightdevice.ArtificialHorizonPane;
import ru.cpb9.geotarget.ui.controls.parameters.linebuilder.LineBuilder;
import ru.cpb9.geotarget.ui.controls.parameters.table.ParametersTable;
import ru.cpb9.geotarget.ui.layers.DeviceTailsLayer;
import ru.cpb9.geotarget.ui.layers.DevicesLayer;
import ru.cpb9.geotarget.ui.layers.GraticuleLayer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Group;
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

        MainPane mainPane = new MainPane(deviceController);
        deviceController.getWorldWind().getPanel().getModel().getLayers().add(new DevicesLayer(I.devices(), deviceController));
        deviceController.getWorldWind().getPanel().getModel().getLayers().add(
                new DeviceTailsLayer(I.deviceTail(), deviceController));
        deviceController.getWorldWind().getPanel().getModel().getLayers().add(new GraticuleLayer(I.graticule()));

        ActorRef localDb = makeActorRef(LocalDbServerActor.class, ActorName.LOCAL_DB_SERVER, Resources.getResource(
                "ru/cpb9/ifdev/local.sqlite"));
        makeActorRef(PositionOrientationUpdateActor.class, ActorName.POSITION_UPDATE_ACTOR, deviceController.getDeviceRegistry());

        MenuBar mainMenu = new MenuBar();
        Menu fileMenu = new Menu(I.file());
        MenuItem exitMenuItem = new MenuItem(I.exit());
        exitMenuItem.setOnAction(e -> requestShutdown());
        fileMenu.getItems().addAll(exitMenuItem);
        mainMenu.getMenus().addAll(fileMenu);
        VBox vBox = new VBox(mainMenu, mainPane);
        VBox.setVgrow(mainPane, Priority.ALWAYS);
        primaryStage.setScene(new Scene(vBox, 1200, 1000));
        primaryStage.setTitle(I.appName());
        primaryStage.show();

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

    private static class MainPane extends BorderPane
    {
        @NotNull
        private final ParametersTree parametersTree;
        @NotNull
        private final DeviceList deviceList;
        @NotNull
        private final ParametersTable parametersTable;
        @NotNull
        private final ArtificialHorizonPane flightDevice;
        @NotNull
        private final LineBuilder lineBuilder;

        public MainPane(@NotNull DeviceController deviceController)
        {
            MavlinkDevice mavlinkDevice = MavlinkDevice.newMavlinkDevice(14550);

            WorldWindNode worldWindNode = deviceController.getWorldWind();
            StackPane stackPane = new StackPane(worldWindNode);

            parametersTable = new ParametersTable(deviceController);
            parametersTree = new ParametersTree(deviceController);
            deviceList = new DeviceList(deviceController);
            flightDevice = new ArtificialHorizonPane(deviceController.getDeviceRegistry());
            lineBuilder = new LineBuilder(deviceController);

            Button layerButton = new Button(I.layers());
            Button treeParamButton = new Button(I.parametersTree());
            Button tableParamButton = new Button(I.parametersTable());
            Button artifHorButton = new Button(I.flightDevice());
            Button deviceListButton = new Button(I.deviceList());
            Button lineBuilderButton = new Button(I.pathBuilder());

            HBox buttonsPanel = new HBox(layerButton, treeParamButton, tableParamButton, artifHorButton, deviceListButton, lineBuilderButton);

            //For rotation
            Group group = new Group(buttonsPanel);
            buttonsPanel.setRotate(90);
            setLeft(group);

            VBox nodesBox = new VBox();

            LayersList layers = new LayersList(worldWindNode);

            SplitPane splitPane = new SplitPane();
            nodesBox.getChildren().add(splitPane);

            splitPane.setOrientation(Orientation.VERTICAL);
            splitPane.getItems().addAll(layers);
            VBox.setVgrow(splitPane, Priority.ALWAYS);

            layerButton.setOnAction(event -> {
                if (splitPane.getItems().contains(layers)) {
                    splitPane.getItems().remove(layers);
                } else {
                    splitPane.getItems().add(layers);
                }
            });
            treeParamButton.setOnAction(event -> {
                if (splitPane.getItems().contains(parametersTable)) {
                    splitPane.getItems().remove(parametersTable);
                } else {
                    splitPane.getItems().add(parametersTable);
                }
            });
            tableParamButton.setOnAction(event -> {
                if (splitPane.getItems().contains(parametersTree)) {
                    splitPane.getItems().remove(parametersTree);
                } else {
                    splitPane.getItems().add(parametersTree);
                }
            });
            artifHorButton.setOnAction(event -> {
                if (splitPane.getItems().contains(flightDevice)) {
                    splitPane.getItems().remove(flightDevice);
                } else {
                    splitPane.getItems().add(flightDevice);
                }
            });
            lineBuilderButton.setOnAction(event -> {
                if (splitPane.getItems().contains(lineBuilder)) {
                    splitPane.getItems().remove(lineBuilder);
                } else {
                    splitPane.getItems().add(lineBuilder);
                }
            });
            deviceListButton.setOnAction(event -> {
                if (splitPane.getItems().contains(deviceList)) {
                    splitPane.getItems().remove(deviceList);
                } else {
                    splitPane.getItems().add(deviceList);
                }
            });

            SplitPane mainPane = new SplitPane();
            mainPane.setOrientation(Orientation.HORIZONTAL);
            mainPane.getItems().addAll(nodesBox, stackPane);
            mainPane.setDividerPositions(0.1);
            setCenter(mainPane);

        }
    }
}
