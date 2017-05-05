package com.graph.api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.graph.json.JsonSupport
import com.graph.models.{Person, PersonWithRelation, Relation}
import com.graph.service.RelationService
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration.DurationInt

class RelationApiSpec extends WordSpec with MockitoSugar with Matchers with ScalatestRouteTest with JsonSupport {

  implicit val defaultTimeout = RouteTestTimeout(10.seconds)

  val mockRelationService = mock[RelationService]

  object RelationApiTest extends RelationsApi {
    val relationService = mockRelationService
  }

  val routes = RelationApiTest.relationRoutes

  "The Relation routes" should {
    "insert relations" in {
      val person1 = Person(id = None, name = "harshit", `type` = "employee", age = 25)
      val person2 = Person(id = None, name = "gaurav", `type` = "employee", age = 26)
      val relation = Relation(person1, person2, "colleague")

      when(mockRelationService.insert(relation)) thenReturn relation.copy(from = person1.copy(id = Some("12345")),
        to = person2.copy(id = Some("4321")))

      val inputJson = """{"from":{"name":"harshit","age":25,"type":"employee"},"to":{"name":"gaurav","age":26,"type":"employee"},"relationType":"colleague"}"""
      Post("/insert", HttpEntity(ContentTypes.`application/json`, inputJson)) ~> routes ~> check {
        handled shouldBe true
        val resp = fromJson(responseAs[String]).extract[Relation]
        //id is vertex id in Graph
        assert(resp.from.id.nonEmpty)
        assert(resp.to.id.nonEmpty)
      }
    }

    "insert relations between two already existing Persons in graph" in {
      val person1 = Person(id = None, name = "harshit", `type` = "employee", age = 25)
      val person2 = Person(id = None, name = "gaurav", `type` = "employee", age = 26)
      val relation = Relation(person1, person2, "colleague")

      val personRelations = PersonWithRelation("12345", "22456", "friends")

      when(mockRelationService.insertById(personRelations)) thenReturn Some(relation.copy(from = person1.copy(id = Some("12345")),
        to = person2.copy(id = Some("22456"))))

      val inputJson = """{"fromId": "12345", "toId": "22456", "relationType": "friends"}"""
      Post("/insert/id", HttpEntity(ContentTypes.`application/json`, inputJson)) ~> routes ~> check {
        handled shouldBe true
        val resp = fromJson(responseAs[String]).extract[Relation]
        assert(resp.from.id.nonEmpty)
        assert(resp.to.id.nonEmpty)
      }
    }

  }

}
