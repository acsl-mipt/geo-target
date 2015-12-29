package ru.mipt.acsl.geotarget

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import ru.mipt.acsl.decode.cpp.generator.{CppSourcesGenerator, CppGeneratorConfiguration}
import ru.mipt.acsl.decode.model.domain.DecodeFqn
import ru.mipt.acsl.decode.model.domain.impl.`type`.DecodeFqnImpl

import scala.collection.immutable.HashMap

/**
 * @author Artem Shein
 */
object OnBoardSourcesGenerator extends LazyLogging {

  def fqn(str: String): DecodeFqn = DecodeFqnImpl.newFromSource(str)

  def main(args : Array[String]) = {
    val config = new CppGeneratorConfiguration(new File("cpp-sources/"),
      ModelRegistry.registry,
      "ru.mipt.acsl.mcc.FlyingDevice",
      HashMap(
        fqn("ru.mipt.acsl.foundation") -> fqn("foundation"),
        fqn("ru.mipt.acsl.fs") -> fqn("fs"),
        fqn("ru.mipt.acsl.identification") -> fqn("identification"),
        fqn("ru.mipt.acsl.mcc") -> fqn("mcc"),
        fqn("ru.mipt.acsl.routing") -> fqn("routing"),
        fqn("ru.mipt.acsl.scripting") -> fqn("scripting"),
        fqn("ru.mipt.acsl.segmentation") -> fqn("segmentation"),
        fqn("ru.mipt.acsl.tm") -> fqn("tm")))
    logger.debug(s"Generating on-board sources to ${config.outputDir.getAbsolutePath}...")
    new CppSourcesGenerator(config).generate()
  }
}
