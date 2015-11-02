package ru.mipt.acsl.geotarget

import java.io.File

import ru.cpb9.geotarget.ModelRegistry
import ru.mipt.acsl.decode.c.generator.{CDecodeSourcesGenerator, CDecodeGeneratorConfiguration}

/**
 * @author Artem Shein
 */
object OnBoardSourcesGenerator {
  def main(args : Array[String]) = {
    new CDecodeSourcesGenerator(new CDecodeGeneratorConfiguration(new File("csources/"), ModelRegistry.getRegistry)).generate()
  }
}
