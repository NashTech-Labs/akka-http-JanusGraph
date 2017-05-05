package com.graph.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.graph.drivers.JanusDB
import com.graph.service.{RelationServiceImpl, SearchServiceImpl}


object GraphApi extends App with RelationsApi with SearchApi {

  implicit val system = ActorSystem("graph-api")
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher

  JanusDB.janusGraph.openManagement()

  val relationService = RelationServiceImpl
  val searchService = SearchServiceImpl

  val routes = relationRoutes ~ searchRoutes

  val bindFuture = Http().bindAndHandle(routes, "localhost", 9000)

}
