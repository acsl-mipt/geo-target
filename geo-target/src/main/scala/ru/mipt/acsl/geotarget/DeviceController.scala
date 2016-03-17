package ru.mipt.acsl.geotarget

import gov.nasa.worldwind.geom.Position
import ru.cpb9.geotarget.ui.controls.WorldWindNode
import ru.cpb9.geotarget.ui.layers.DevicesLayer

import scala.collection.JavaConversions._

/**
  * @author Artem Shein
  */
trait DeviceController {
  def deviceRegistry: DeviceRegistry
  def worldWind: WorldWindNode
  def navigateToActiveDevice(): Unit
  def requestRepaint(): Unit
}

object DeviceController {
  def apply(deviceRegistry: DeviceRegistry, worldWind: WorldWindNode): DeviceController =
    new DeviceControllerImpl(deviceRegistry, worldWind)
}

private class DeviceControllerImpl(val deviceRegistry: DeviceRegistry, val worldWind: WorldWindNode)
  extends DeviceController {

  override def navigateToActiveDevice(): Unit = {
    for (activeDevice <- deviceRegistry.activeDeviceProperty.getValue) {
      val devicePositions = activeDevice.getDevicePositions
      val positionOrientation = devicePositions.get(devicePositions.size() - 1)
      val position = positionOrientation.getPosition
      val latitude = position.latitude.degrees
      val longitude = position.longitude.degrees
      val altitude = position.elevation
      val elevation = (altitude + 1000) * 10
      worldWind.getPanel.getView.goTo(Position.fromDegrees(latitude, longitude, elevation), elevation)
      asScalaIterator(worldWind.getPanel.getModel.getLayers.iterator()).foreach {
        case l: DevicesLayer => asScalaIterator(l.getDeviceCones.iterator()).foreach(_.setConeSize(elevation / 10))
        case _ =>
      }
    }
  }

  override def requestRepaint(): Unit = worldWind.getPanel.redraw()
}
