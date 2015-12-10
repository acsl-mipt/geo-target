package ru.mipt.acsl.geotarget

import com.typesafe.scalalogging.LazyLogging
import ru.mipt.acsl.decode.model.domain.DecodeRegistry
import ru.mipt.acsl.decode.model.domain.impl.DecodeModelResolver
import ru.mipt.acsl.decode.model.provider.{DecodeSqlProviderConfiguration, DecodeSqlProvider}
import ru.mipt.acsl.decode.modeling.ResolvingMessage
import ru.mipt.acsl.decode.parser.{DecodeSourceProvider, DecodeSourceProviderConfiguration}
import scala.collection.JavaConversions._

/**
  * @author Artem Shein
  */
object ModelRegistry extends LazyLogging {

  private val newRegistry = newResourceProvider.apply()
  private val resolvingResult = DecodeModelResolver.resolve(newRegistry)
  if (resolvingResult.hasError)
    resolvingResult.getMessages.toList.foreach((m: ResolvingMessage) => logger.error(m.getText))

  val registry: DecodeRegistry = newRegistry

  def newResourceProvider: () => DecodeRegistry = {
    val provider = new DecodeSourceProvider()
    val config = new DecodeSourceProviderConfiguration("ru/mipt/acsl/decode")
    () => provider.provide(config)
  }

  private val RESOURCE = getClass.getResource("ru/mipt/acsl/decode/local.sqlite")

  def newSqlProvider: () => DecodeRegistry = {
    val provider = new DecodeSqlProvider
    val config = new DecodeSqlProviderConfiguration("jdbc:sqlite::resource:" + RESOURCE)
    () => provider.provide(config)
  }
}
