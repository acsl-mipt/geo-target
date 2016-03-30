package ru.mipt.acsl.geotarget

import java.io.File

import ru.mipt.acsl.decode.doc.generator.html.{HtmlDocGenerator, HtmlDocGeneratorConfiguration}
import ru.mipt.acsl.decode.model.domain.impl.naming.Fqn

/**
  * @author Artem Shein
  */
object PhotonHtmlDocsGenerator {
  def main(args: Array[String]) = {
    val config = HtmlDocGeneratorConfiguration(new File("Photon_components.html"), ModelRegistry.registry,
      exclude = Set(Fqn.newFromSource("test")))
    new HtmlDocGenerator(config).generate()
  }
}
