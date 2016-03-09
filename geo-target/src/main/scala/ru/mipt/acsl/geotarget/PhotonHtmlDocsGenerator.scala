package ru.mipt.acsl.geotarget

import java.io.File

import ru.mipt.acsl.decode.docs.generator.html.{HtmlDocsGenerator, HtmlDocsGeneratorConfiguration}
import ru.mipt.acsl.decode.model.domain.impl.naming.Fqn

/**
  * @author Artem Shein
  */
object PhotonHtmlDocsGenerator {
  def main(args: Array[String]) = {
    val config = HtmlDocsGeneratorConfiguration(new File("Photon_components.html"), ModelRegistry.registry,
      exclude = Set(Fqn.newFromSource("test")))
    new HtmlDocsGenerator(config).generate()
  }
}
