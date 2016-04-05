package ru.mipt.acsl.geotarget

import ru.mipt.acsl.decode.persistence.sqlite.SchemaCreator

/**
  * @author Artem Shein
  */
object PrintDecodeSchema {
  def main(args: Array[String]):Unit = {
    SchemaCreator.tables.foreach(t => println(t + ";"))
  }
}
