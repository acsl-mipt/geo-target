package ru.mipt.acsl.geotarget

import com.typesafe.scalalogging.LazyLogging
import ru.mipt.acsl.decode.model.domain.registry.Registry
import ru.mipt.acsl.decode.modeling.ErrorLevel
import ru.mipt.acsl.decode.parser.{DecodeSourceProvider, DecodeSourceProviderConfiguration}

/**
  * @author Artem Shein
  */
object ModelRegistry extends LazyLogging {

  private val newRegistry = newResourceProvider()
  private val resolvingResult = newRegistry.resolve()
  if (resolvingResult.exists(_.level == ErrorLevel))
    resolvingResult.foreach(msg => logger.error(msg.text))

  val registry: Registry = newRegistry

  def newResourceProvider: () => Registry = {
    val provider = new DecodeSourceProvider()
    val config = new DecodeSourceProviderConfiguration("/ru/mipt/acsl/decode")
    () => provider.provide(config)
  }
}
