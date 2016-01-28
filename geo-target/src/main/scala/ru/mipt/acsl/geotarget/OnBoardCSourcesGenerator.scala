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
    val config = new CGeneratorConfiguration(new File("photon-gen/"),
      ModelRegistry.registry,
      "ru.mipt.acsl.photon.Main",
      HashMap(
        fqn("decode") -> Some(fqn("mcc.photon.decode")),
        fqn("ru.mipt.acsl.photon") -> Some(fqn("mcc.photon")),
        fqn("ru.mipt.acsl.foundation") -> Some(fqn("mcc.photon.foundation")),
        fqn("ru.mipt.acsl.fs") -> Some(fqn("mcc.photon.fs")),
        fqn("ru.mipt.acsl.identification") -> Some(fqn("mcc.photon.identification")),
        fqn("ru.mipt.acsl.mcc") -> Some(fqn("mcc.photon")),
        fqn("ru.mipt.acsl.routing") -> Some(fqn("mcc.photon.routing")),
        fqn("ru.mipt.acsl.scripting") -> Some(fqn("mcc.photon.scripting")),
        fqn("ru.mipt.acsl.segmentation") -> Some(fqn("mcc.photon.segmentation")),
        fqn("ru.mipt.acsl.tm") -> Some(fqn("mcc.photon.tm"))),
    prologEpilogPath = Some("mcc/modeling"), isSingleton = true)
    logger.debug(s"Generating on-board sources to ${config.outputDir.getAbsolutePath}...")
    new CSourcesGenerator(config).generate()
  }
}