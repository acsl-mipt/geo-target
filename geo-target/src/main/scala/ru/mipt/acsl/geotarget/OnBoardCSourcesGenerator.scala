package ru.mipt.acsl.geotarget

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import ru.mipt.acsl.decode.c.generator.{CSourcesGenerator, CGeneratorConfiguration}
import ru.mipt.acsl.decode.model.domain.DecodeFqn
import ru.mipt.acsl.decode.model.domain.impl.`type`.DecodeFqnImpl

import scala.collection.immutable.HashMap

/**
  * @author Artem Shein
  */
object OnBoardCSourcesGenerator extends LazyLogging {

  def fqn(str: String): DecodeFqn = DecodeFqnImpl.newFromSource(str)

  def main(args : Array[String]) = {
    val config = new CGeneratorConfiguration(new File("gen/"),
      ModelRegistry.registry,
      "ru.mipt.acsl.photon.Main",
      HashMap(
        fqn("decode") -> Some(fqn("photon.decode")),
        fqn("ru.mipt.acsl.photon") -> Some(fqn("photon")),
        fqn("ru.mipt.acsl.foundation") -> Some(fqn("photon.foundation")),
        fqn("ru.mipt.acsl.fs") -> Some(fqn("photon.fs")),
        fqn("ru.mipt.acsl.identification") -> Some(fqn("photon.identification")),
        fqn("ru.mipt.acsl.mcc") -> Some(fqn("photon")),
        fqn("ru.mipt.acsl.routing") -> Some(fqn("photon.routing")),
        fqn("ru.mipt.acsl.scripting") -> Some(fqn("photon.scripting")),
        fqn("ru.mipt.acsl.segmentation") -> Some(fqn("photon.segmentation")),
        fqn("ru.mipt.acsl.tm") -> Some(fqn("photon.tm"))),
    prologEpilogPath = Some("photon"), isSingleton = true)
    logger.debug(s"Generating on-board sources to ${config.outputDir.getAbsolutePath}...")
    new CSourcesGenerator(config).generate()
  }
}