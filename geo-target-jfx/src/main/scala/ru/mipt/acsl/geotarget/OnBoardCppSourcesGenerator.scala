package ru.mipt.acsl.geotarget

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import ru.mipt.acsl.decode.cpp.generator.{CppGeneratorConfiguration, CppSourceGenerator}
import ru.mipt.acsl.decode.model.domain.impl.naming.Fqn
import ru.mipt.acsl.decode.model.domain.pure.naming.Fqn
import ru.mipt.acsl.decode.parser.ModelRegistry

import scala.collection.immutable.HashMap

/**
 * @author Artem Shein
 */
object OnBoardCppSourcesGenerator extends LazyLogging {

  def fqn(str: String): Fqn = Fqn.newFromSource(str)

  def main(args : Array[String]) = {
    val config = new CppGeneratorConfiguration(new File("decode-gen/"),
      ModelRegistry.registry(getClass.getClassLoader),
      "ru.mipt.acsl.mcc.FlyingDevice",
      HashMap(
        fqn("ru.mipt.acsl.foundation") -> fqn("mcc.decode.foundation"),
        fqn("ru.mipt.acsl.fs") -> fqn("mcc.decode.fs"),
        fqn("ru.mipt.acsl.identification") -> fqn("mcc.decode.identification"),
        fqn("ru.mipt.acsl.mcc") -> fqn("mcc.decode"),
        fqn("ru.mipt.acsl.routing") -> fqn("mcc.decode.routing"),
        fqn("ru.mipt.acsl.scripting") -> fqn("mcc.decode.scripting"),
        fqn("ru.mipt.acsl.segmentation") -> fqn("mcc.decode.segmentation"),
        fqn("ru.mipt.acsl.tm") -> fqn("mcc.decode.tm")))
    logger.debug(s"Generating on-board sources to ${config.outputDir.getAbsolutePath}...")
    new CppSourceGenerator(config).generate()
  }
}
