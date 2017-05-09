package com.graph.api

import akka.http.scaladsl.server.{Directives, Route}
import com.graph.serializer.JsonSupport
import com.graph.models.{PersonWithRelation, Relation}
import com.graph.service.Relations

import akka.http.scaladsl.model.StatusCodes.Created

trait RelationsApi extends Directives with JsonSupport {

  val relationService: Relations

  def relationRoutes = insert ~ insertById

  def insert: Route = path("insert") {
    post {
      entity(as[String]) { relationStr =>
        complete {
          val resp = relationService.insert(fromJson(relationStr).extract[Relation])
          Created -> toJson(resp)
        }
      }
    }

  }

  def insertById: Route = path("insert" / "id") {
    post {
      entity(as[String]) { personWithRelationStr =>
        complete {
          val resp = relationService.insertById(fromJson(personWithRelationStr).extract[PersonWithRelation])
          Created -> toJson(resp)
        }

      }
    }
  }

}
