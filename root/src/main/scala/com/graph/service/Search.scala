package com.graph.service

import com.graph.drivers.{GraphFactory, JanusGraphFactory}
import com.graph.models.Person
import gremlin.scala._

trait Search {
  this: GraphFactory =>

  def searchByName(name: String): List[Person] = withTransaction { implicit graph =>
    graph.V().hasLabel("person").has(Key[String]("name"), name).toList.map { personV =>
      personV.toCC[Person]
    }
  }

  def searchById(id: String): Option[Person] = withTransaction { implicit graph =>
    graph.V(id).hasLabel("person").headOption.map(_.toCC[Person])
  }

  def searchByType(`type`: String): List[Person] = withTransaction { implicit graph =>
    graph.V().hasLabel("person").has(Key[String]("type"), `type`).toList.map(_.toCC[Person])
  }

  def findRelation(relationType: String, person: Person): List[Person] = withTransaction { implicit graph =>
    graph.V().hasLabel("person").has(Key[String]("name"), person.name).has(Key[Int]("age"), person.age).has(Key[String]("type"), person.`type`)
      .both(relationType).toCC[Person].toList()
  }

}

object SearchServiceImpl extends Search with JanusGraphFactory
