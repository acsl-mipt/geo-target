package ru.mipt.acsl.geotarget

import com.typesafe.scalalogging.LazyLogging
import ru.mipt.acsl.decode.model.domain.Registry
import ru.mipt.acsl.decode.model.domain.impl.DecodeModelResolver
import ru.mipt.acsl.decode.model.provider.{DecodeSqlProviderConfiguration, DecodeSqlProvider}
import ru.mipt.acsl.decode.modeling.ErrorLevel
import ru.mipt.acsl.decode.parser.{DecodeSourceProvider, DecodeSourceProviderConfiguration}
import scala.collection.JavaConversions._

/**
  * @author Artem Shein
  */
object ModelRegistry extends LazyLogging {

  private val newRegistry = newResourceProvider.apply()
  private val resolvingResult = DecodeModelResolver.resolve(newRegistry)
  if (resolvingResult.exists(_.level == ErrorLevel))
    resolvingResult.foreach(msg => logger.error(msg.text))

  val registry: Registry = newRegistry

  def newResourceProvider: () => Registry = {
    val provider = new DecodeSourceProvider()
    val config = new DecodeSourceProviderConfiguration("/ru/mipt/acsl/decode")
    () => provider.provide(config)
  }

  private val RESOURCE = getClass.getResource("ru/mipt/acsl/decode/local.sqlite")

  def newSqlProvider: () => Registry = {
    val provider = new DecodeSqlProvider
    val config = new DecodeSqlProviderConfiguration("jdbc:sqlite::resource:" + RESOURCE)
    () => provider.provide(config)
  }
}
