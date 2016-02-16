package ru.mipt.acsl.geotarget

import java.util.Locale

import c10n.C10N
import c10n.annotations.DefaultC10NAnnotations
import com.typesafe.scalalogging.{StrictLogging, LazyLogging}
import gov.nasa.worldwind.layers.Layer
import org.apache.commons.lang3.exception.ExceptionUtils
import ru.cpb9.geotarget.ui.controls.parameters.flightdevice.ArtificialHorizonPane
import ru.cpb9.geotarget.ui.controls.parameters.table.ParametersTable
import ru.cpb9.geotarget.ui.controls.parameters.tree.ParametersTree
import ru.cpb9.geotarget.ui.{LayersList, Widget, AddDeviceWidget, GeoTargetModel}
import ru.cpb9.geotarget.ui.controls.{DeviceList, WorldWindNode}
import ru.cpb9.geotarget.ui.layers.{GraticuleLayer, DeviceTailsLayer, DevicesLayer}
import ru.cpb9.geotarget.{Messages, ActorsRegistry}
import ru.cpb9.geotarget.akka.ActorName
import ru.cpb9.geotarget.akka.server.TmServerActor
import scala.collection.JavaConversions._

import scalafx.application.{Platform, JFXApp}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.Observable
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.control.{CheckMenuItem, MenuBar, Menu, MenuItem}
import scalafx.Includes._
import scalafx.scene.layout.{Priority, VBox, Pane}

/**
  * @author Artem Shein
  */
object GeoTargetApplication extends JFXApp with StrictLogging {

  C10N.configure(new DefaultC10NAnnotations)
  Locale.setDefault(new Locale("ru"))

  private val I = C10N.get(classOf[Messages])
  private val ACTORS_REGISTRY: ActorsRegistry = ActorsRegistry.getInstance
  private val tmServerActorRef = ACTORS_REGISTRY.makeActor(classOf[TmServerActor], ActorName.TM_SERVER.getName)

  val deviceController = DeviceController(new DeviceRegistryImpl(tmServerActorRef),
    new WorldWindNode(new GeoTargetModel()))

  deviceController.worldWind.getPanel.getModel.getLayers.addAll(Seq(
    new DevicesLayer(I.devices(), deviceController),
    new DeviceTailsLayer(I.deviceTail(), deviceController),
    new GraticuleLayer(I.graticule())
  ))

  val addDeviceWidget = new AddDeviceWidget(deviceController, tmServerActorRef)
  val parametersTreeWidget = new Widget(I.parametersTree(), new ParametersTree(deviceController))
  val deviceListWidget = new Widget(I.deviceList(), new DeviceList(deviceController, tmServerActorRef))
  val artificialHorizonWidget = new Widget(I.artificialHorizon(), new ArtificialHorizonPane(deviceController.deviceRegistry))
  val worldWindNode = deviceController.worldWind
  val layerListWidget = new Widget(I.layerList(), new LayersList(worldWindNode))

  val fileMenu = new Menu(I.file()) {
    items = Seq(
      new MenuItem(I.exit()) {
        onAction = (ae: ActionEvent) => requestShutdown()
      })
  }
  val viewMenu = new Menu(I.view()) {
    items = Seq(
      newBindedToWidgetVisibilityCheckMenuItem(addDeviceWidget),
      newBindedToWidgetVisibilityCheckMenuItem(parametersTreeWidget),
      newBindedToWidgetVisibilityCheckMenuItem(deviceListWidget),
      newBindedToWidgetVisibilityCheckMenuItem(artificialHorizonWidget),
      newBindedToWidgetVisibilityCheckMenuItem(layerListWidget))
  }

  val mainMenu = new MenuBar() {
    menus ++= Seq(fileMenu, viewMenu)
  }

  val mainPane = new Pane() {
    children.addAll(Seq(worldWindNode, addDeviceWidget, parametersTreeWidget, deviceListWidget,
      artificialHorizonWidget, layerListWidget))
  }

  val sizeUpdater = (o: Observable) => {
    worldWindNode.resize(mainPane.getWidth, mainPane.getHeight)
    worldWindNode.getPanel.setSize(mainPane.getWidth.toInt, mainPane.getHeight.toInt)
  }

  mainPane.height.onInvalidate(sizeUpdater)
  mainPane.width.onInvalidate(sizeUpdater)

  val vBox = new VBox(mainMenu, mainPane)
  VBox.setVgrow(mainPane, Priority.Always)
  stage = new PrimaryStage {
    scene = new Scene(vBox, 1200, 1000)
    title = I.appName()
  }

  def newBindedToWidgetVisibilityCheckMenuItem(widget: Widget): CheckMenuItem = {
    val menuItem = new CheckMenuItem(widget.getTitle)
    widget.visibleProperty().bind(menuItem.selectedProperty())
    widget.getCloseButton.setOnAction((e: ActionEvent) => menuItem.setSelected(false))
    menuItem
  }

  def requestShutdown() {
    try ACTORS_REGISTRY.shutdown() finally {
      Platform.exit()
    }
  }
}
