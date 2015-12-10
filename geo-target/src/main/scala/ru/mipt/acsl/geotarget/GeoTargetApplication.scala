package ru.mipt.acsl.geotarget

import java.util.Locale

import javafx.application.Application
import javafx.stage.Stage
import c10n.C10N
import c10n.annotations.DefaultC10NAnnotations
import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.cpb9.geotarget.ui.GeoTargetModel
import ru.cpb9.geotarget.ui.controls.WorldWindNode
import ru.cpb9.geotarget.{Messages, SimpleDeviceRegistry, SimpleDeviceController, ActorsRegistry}
import ru.cpb9.geotarget.akka.ActorName
import ru.cpb9.geotarget.akka.server.TmServerActor

/**
  * @author Artem Shein
  */
object GeoTargetApplication extends Application with LazyLogging {
/*
  C10N.configure(new DefaultC10NAnnotations)
  Locale.setDefault(new Locale("ru"))

  private val I = C10N.get(classOf[Messages])
  private val ACTORS_REGISTRY: ActorsRegistry = ActorsRegistry.getInstance
  private val tmServerActorRef = ACTORS_REGISTRY.makeActor(classOf[TmServerActor], ActorName.TM_SERVER.getName)

  val deviceController = new SimpleDeviceController(SimpleDeviceRegistry.newInstance(tmServerActorRef),
    new WorldWindNode(new GeoTargetModel()))

  def main(args: Array[String]): Unit = {
    try {
      launch(args)
    } catch {
      case e: Throwable =>
        logger.error(ExceptionUtils.getStackTrace(e))
        System.exit(1)
    }
    System.exit(0)
  }

  def start(primaryStage: Stage): Unit = {

  }*/
/*
private static final Logger LOG = LoggerFactory.getLogger(GeoTargetApplication.class);

    @Override
    public void start(@NotNull Stage primaryStage) throws Exception
    {
        LOG.info("Starting...");
        List<String> parameters = getParameters().getUnnamed();

        deviceController.getWorldWind().getPanel().getModel().getLayers().add(new DevicesLayer(I.devices(), deviceController));
        deviceController.getWorldWind().getPanel().getModel().getLayers().add(
                new DeviceTailsLayer(I.deviceTail(), deviceController));
        deviceController.getWorldWind().getPanel().getModel().getLayers().add(new GraticuleLayer(I.graticule()));

        Widget addDeviceWidget = new AddDeviceWidget(deviceController, tmServerActorRef);
        //Widget parametersTableWidget = new Widget(I.parametersTable(), new ParametersTable(deviceController));
        Widget parametersTreeWidget = new Widget(I.parametersTree(), new ParametersTree(deviceController));
        Widget deviceListWidget = new Widget(I.deviceList(), new DeviceList(deviceController, tmServerActorRef));
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
        //parametersTableWidget.visibleProperty().bind(parametersTableWidgetMenuItem.selectedProperty());
        viewMenu.getItems().addAll(
                newBindedToWidgetVisibilityCheckMenuItem(addDeviceWidget),
                //newBindedToWidgetVisibilityCheckMenuItem(parametersTableWidget),
                newBindedToWidgetVisibilityCheckMenuItem(parametersTreeWidget),
                newBindedToWidgetVisibilityCheckMenuItem(deviceListWidget),
                newBindedToWidgetVisibilityCheckMenuItem(artificialHorizonWidget),
                newBindedToWidgetVisibilityCheckMenuItem(layerListWidget));

        mainMenu.getMenus().addAll(fileMenu, viewMenu);

        Pane mainPane = new Pane();
        mainPane.getChildren().addAll(worldWindNode, addDeviceWidget,
                //parametersTableWidget,
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
 */
  override def start(primaryStage: Stage) {}
}
