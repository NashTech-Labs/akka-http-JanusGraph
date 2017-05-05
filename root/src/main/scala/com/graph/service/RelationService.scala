package com.graph.service

import com.graph.drivers.{GraphFactory, JanusGraphFactory}
import com.graph.models.{Person, PersonWithRelation, Relation}
import gremlin.scala._

trait RelationService {
  this: GraphFactory =>

  def insert(relation: Relation): Relation = withTransaction { implicit graph =>
    val fromV = graph + relation.from
    val toV = graph + relation.to
    fromV --- relation.relationType --> toV
    relation.copy(from = fromV.toCC[Person], to = toV.toCC[Person])
  }

  def insertById(personWithRelation: PersonWithRelation): Option[Relation] = withTransaction { implicit graph =>
    import personWithRelation._
    for {
      fromV <- graph.V(fromId).hasLabel("person").headOption
      toV <- graph.V(toId).hasLabel("person").headOption
    } yield {
      fromV --- relationType --> toV
      Relation(fromV.toCC[Person], toV.toCC[Person], relationType)
    }
  }

}


object RelationServiceImpl extends RelationService with JanusGraphFactory
