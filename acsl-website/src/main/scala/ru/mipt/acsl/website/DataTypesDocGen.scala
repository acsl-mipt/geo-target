package ru.mipt.acsl.website

import ru.mipt.acsl.decode.generator.doc.Dsl._

/**
  * @author Artem Shein
  */
object DataTypesDocGen {

  def main(args: Array[String]): Unit = {

    println("Generating document...")

    document("Базовое пространство типов").
      section("Состав пространства")(
        p("Пространство типов содержит перечень типов данных и описание их представления."),
        p("Пространство типов включает следующие типы:", ul(li())))

  }

}
