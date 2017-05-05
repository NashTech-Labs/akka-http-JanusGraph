package com.graph.api

import akka.http.scaladsl.server.{Directives, Route}
import com.graph.json.JsonSupport
import com.graph.models.Person
import com.graph.service.SearchService


trait SearchApi extends Directives with JsonSupport {


  val searchService: SearchService

  def searchRoutes: Route = searchByName ~ searchByPersonType ~ searchByPersonAndRelation

  def searchByName: Route = path("search") {
    get {
      parameters('name) { name =>
        complete {
          val res = searchService.searchByName(name)
          toJson(res)
        }

      }
    }
  }

  def searchByPersonType: Route = path("search") {
    get {
      parameters('type) { `type`=>
        complete {
          val res = searchService.searchByType(`type`)
          toJson(res)
        }
      }
    }
  }

  def searchByPersonAndRelation: Route = path("search") {
    post {
      parameters('relationType) { relation =>
        entity(as[String]) { personStr =>
          complete {
            val person = fromJson(personStr).extract[Person]
            val r = searchService.findRelation(relation, person)
            toJson(r)
          }
        }

      }
    }
  }

}
