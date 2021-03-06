package main

import main.feature._
import com.codahale.jerkson.Json._

object ElemFormat {
  //Extract methods to get data from different type value
  //obtained from loading json object in a Map 
  def extractFromArray(key: String, m: Map[String, Any], default: String): List[String] =
    m.get(key).getOrElse("[" + default + ",]").toString.drop(1).dropRight(1).split(",").toList

  def extractFromValue(key: String, m: Map[String, Any], default: String): String = {
    val s = m.get(key).getOrElse(default).toString
    key match {
      case "IP" => IP.toLong(s).toString
      case _ => if (s.isEmpty()) "0.0" else s  
    }
  }
  //TODO: for map if many 
  def extractFromMap(key: String, m: Map[String, Any], default: String) : String =
    m.get(key).getOrElse("0=" + default + "}").toString.split("=")(1).split("}")(0)

  def construct1(line: String): Elem = {
    val m = parse[Map[String, Any]](line)
    Elem(
      extractFromMap("_id", m, "0"),
      List(
        Numeric(typeName = "time", List(extractFromMap("date", m, "0.0").toDouble)),
        Numeric(typeName = "size", List(extractFromValue("size", m, "0.0").toDouble)),
        Numeric(typeName = "space", (
          List(
            extractFromValue("long", m, "0.0").toDouble,
            extractFromValue("lat", m, "0.0").toDouble))),
        Numeric(typeName = "IP", List(extractFromValue("IP", m, "0.0.0.0").toDouble))
        ),
      List(
        //Categorical(typeName = "IP", (extractFromValue("IP", m, "_"))),
        Categorical(typeName = "bot", extractFromValue("bot", m, "_"),
          extractFromValue("bot", m, "_").getBytes("UTF-8").map(_.asInstanceOf[Int])),
        Categorical(typeName = "uri", extractFromArray("uri", m, "_")(0),
          extractFromArray("uri", m, "_")(0).getBytes("UTF-8").map(_.asInstanceOf[Int]))
        ))
  }


  def construct2(line: String): Elem = {
    val m = parse[Map[String, Any]](line)
    Elem(
      extractFromMap("_id", m, "0"),
      List(
        Numeric(typeName = "time", List(extractFromMap("date", m, "0.0").toDouble)),
        Numeric(typeName = "space", (
          List(
            extractFromValue("long", m, "0.0").toDouble,
            extractFromValue("lat", m, "0.0").toDouble)))),
      List())
  }
}