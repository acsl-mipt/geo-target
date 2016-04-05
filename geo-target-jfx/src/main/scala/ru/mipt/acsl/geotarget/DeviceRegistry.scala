package ru.mipt.acsl.geotarget

import javafx.beans.property.{ObjectProperty, SimpleObjectProperty}
import javax.swing.SwingUtilities

import akka.actor.ActorRef
import gov.nasa.worldwind.geom.Position
import ru.cpb9.device.modeling.flying.{Orientation, PositionOrientation}
import ru.cpb9.geotarget.akka.ActorName
import ru.cpb9.geotarget.akka.client.TmClientActor
import ru.cpb9.geotarget.akka.messages.{AllMessagesSubscribe, TmMessageValues}
import ru.cpb9.geotarget.ActorsRegistry
import ru.cpb9.geotarget.model.Device
import ru.mipt.acsl.MotionComponent
import ru.mipt.acsl.device.modeling.KnownTmMessages

import scalafx.collections.ObservableBuffer

/**
  * @author Artem Shein
  */

trait DeviceRegistry {
  def activeDeviceProperty: ObjectProperty[Option[Device]]
  def devices: ObservableBuffer[Device]
  def activeDevice_= (activeDevice: Option[Device]): Unit = activeDeviceProperty.set(activeDevice)
  def activeDevice: Option[Device] = activeDeviceProperty.get
}

object DeviceRegistry {
  def apply(tmServer: ActorRef): DeviceRegistry = new DeviceRegistryImpl(tmServer)
}

private class DeviceRegistryImpl(val tmServer: ActorRef) extends DeviceRegistry {
  val devices: ObservableBuffer[Device] = new ObservableBuffer[Device]()
  val activeDeviceProperty: ObjectProperty[Option[Device]] = new SimpleObjectProperty(Option.empty[Device])
  ActorsRegistry.getInstance().makeActor(classOf[PositionOrientationUpdateActor],
    ActorName.POSITION_UPDATE_ACTOR.getName, this, tmServer)
}

private class PositionOrientationUpdateActor(val deviceRegistry: DeviceRegistry, tmServer: ActorRef)
  extends TmClientActor(tmServer) {

  override def preStart() = tmServer.tell(AllMessagesSubscribe.INSTANCE, getSelf())

  override def onReceive(o: Any) {
    o match {
      case message: TmMessageValues[_] =>
        if (message.getMessage.equals(KnownTmMessages.MotionAll))
        {
          val a = message.getValues.asInstanceOf[MotionComponent.AllMessage]
          deviceRegistry.devices.find(_.getDeviceGuid.get().equals(message.getDeviceGuid))
            .foreach(d =>
              SwingUtilities.invokeLater(new Runnable {
                override def run(): Unit = d.getDevicePositions.add(new PositionOrientation(
                  Position.fromDegrees(a.getLatitude, a.getLongitude, a.getAltitude),
                  Orientation.fromDegrees(a.getHeading, a.getPitch, a.getRoll)))}))
        }
      case _ => unhandled(o)
    }
  }
}
