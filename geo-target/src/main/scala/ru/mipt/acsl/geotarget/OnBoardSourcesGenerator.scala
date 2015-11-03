package ru.mipt.acsl.geotarget

import java.io.File

import ru.cpb9.geotarget.ModelRegistry
import ru.mipt.acsl.decode.c.generator.{CDecodeSourcesGenerator, CDecodeGeneratorConfiguration}
import ru.mipt.acsl.decode.model.domain.DecodeFqn
import ru.mipt.acsl.decode.model.domain.impl.ImmutableDecodeFqn

import scala.collection.immutable.HashMap

/**
 * @author Artem Shein
 */
object OnBoardSourcesGenerator {

  def fqn(str: String): DecodeFqn = ImmutableDecodeFqn.newInstanceFromSource(str)

  def main(args : Array[String]) = {
    new CDecodeSourcesGenerator(new CDecodeGeneratorConfiguration(new File("csources/"),
      ModelRegistry.getRegistry,
      HashMap(
        fqn("ru.mipt.acsl.foundation") -> fqn("foundation"),
        fqn("ru.mipt.acsl.fs") -> fqn("fs"),
        fqn("ru.mipt.acsl.identification") -> fqn("identification"),
        fqn("ru.mipt.acsl.mcc") -> fqn("mcc"),
        fqn("ru.mipt.acsl.routing") -> fqn("routing"),
        fqn("ru.mipt.acsl.scripting") -> fqn("scripting"),
        fqn("ru.mipt.acsl.segmentation") -> fqn("segmentation"),
        fqn("ru.mipt.acsl.tm") -> fqn("tm")))).generate()
  }
}
