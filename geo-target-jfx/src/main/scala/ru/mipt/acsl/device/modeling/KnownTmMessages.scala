package ru.mipt.acsl.device.modeling

import ru.mipt.acsl._
import ru.mipt.acsl.decode.parser.ModelRegistry

/**
  * Created by metadeus on 18.03.16.
  */
object KnownTmMessages {
  private val Registry = ModelRegistry.registry
  val MotionAll = Registry.statusMessageOrFail(MotionComponent.AllMessage.FQN)
  val DeviceAll = Registry.statusMessageOrFail(DeviceComponent.AllMessage.FQN)
  val DogPointAll = Registry.statusMessageOrFail(DogPointComponent.AllMessage.FQN)
  val ModeAll = Registry.statusMessageOrFail(ModeComponent.AllMessage.FQN)
  val RouteAll = Registry.statusMessageOrFail(RouteComponent.AllMessage.FQN)
}
