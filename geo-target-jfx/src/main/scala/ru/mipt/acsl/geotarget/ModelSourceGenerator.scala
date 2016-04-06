package ru.mipt.acsl.geotarget

import java.io.File

import ru.mipt.acsl.decode.java.generator.JavaDecodeSourcesGeneratorConfiguration
import ru.mipt.acsl.decode.parser.ModelRegistry

/**
  * @author Artem Shein
  */
object ModelSourceGenerator {
  def main(args: Array[String]): Unit = {
    val config = new JavaDecodeSourcesGeneratorConfiguration(new File("."),
      ModelRegistry.registry(getClass.getClassLoader))
    generate(config)
  }
  def generate(config: JavaDecodeSourcesGeneratorConfiguration): Unit = {
    sys.error("not implemented")
  }
}
