package com.graph.models

import gremlin.scala.{id, label}

@label("person")
case class Person(
                   @id id: Option[String] = None,
                   name: String,
                   age: Int,
                   `type`: String
                 )

case class Relation(
                     from: Person,
                     to: Person,
                     relationType: String
                   )

case class PersonWithRelation(
                               fromId: String,
                               toId: String,
                               relationType: String
                             )
