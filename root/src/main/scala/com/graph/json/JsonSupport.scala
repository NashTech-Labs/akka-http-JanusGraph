package com.graph.json

import org.json4s.JValue
import org.json4s.native.JsonMethods.parse
import org.json4s.native.Serialization.write

trait JsonSupport {

  implicit val formats = org.json4s.DefaultFormats

  def toJson[T <: AnyRef](input: T): String = write(input)

  def fromJson(value: String): JValue = parse(value)
}
