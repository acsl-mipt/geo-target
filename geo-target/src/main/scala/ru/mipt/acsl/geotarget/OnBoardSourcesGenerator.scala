package ru.mipt.acsl.geotarget

import java.io.File

import ru.cpb9.geotarget.ModelRegistry
import ru.mipt.acsl.decode.c.generator.{CDecodeSourcesGenerator, CDecodeGeneratorConfiguration}

/**
 * @author Artem Shein
 */
class OnBoardSourcesGenerator {
  def main(args : Array[String]) = {
    new CDecodeSourcesGenerator(Some(new CDecodeGeneratorConfiguration(new File("csources/"), ModelRegistry.getRegistry))).generate()
  }
}
