package com.graph.api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import com.graph.serializer.JsonSupport
import com.graph.models.Person
import com.graph.service.Search
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration.DurationInt

class SearchApiSpec extends WordSpec with MockitoSugar with Matchers with ScalatestRouteTest with JsonSupport {

  implicit val defaultTimeout = RouteTestTimeout(10.seconds)

  val mockSearchService = mock[Search]
  val routes = SearchApiTest.searchRoutes

  object SearchApiTest extends SearchApi {
    val searchService = mockSearchService
  }

  "The Search routes" should {

    "be able to search by Person's name" in {
      val person = Person(id = Some("12467"), "Harshit", 25, "employee")

      when(mockSearchService.searchByName("Harshit")) thenReturn List(person)

      Get("/search?name=Harshit") ~> routes ~> check {
        handled shouldBe true
        val resp = fromJson(responseAs[String]).extract[List[Person]]
        resp.size shouldEqual 1
        resp.head shouldEqual person
      }
    }

    "be able to search by Person type" in {
      val person = Person(id = Some("12467"), "Harshit", 25, "employee")
      val person1 = Person(id = Some("112467"), "Gaurav", 26, "employee")

      when(mockSearchService.searchByType("employee")) thenReturn List(person, person1)

      Get("/search?type=employee") ~> routes ~> check {
        handled shouldBe true
        val resp = fromJson(responseAs[String]).extract[List[Person]]
        resp.size shouldEqual 2
        resp.sortBy(_.id) shouldEqual List(person1, person)
      }
    }

    "be able to search by Person type and relation" in {
      val person = Person(id = Some("12467"), "Harshit", 25, "employee")
      val person1 = Person(id = Some("112467"), "Gaurav", 26, "employee")

      when(mockSearchService.findRelation("friend", person.copy(id = None))) thenReturn List(person, person1)

      val inputJson = """{"name": "Harshit", "age": 25, "type": "employee"}"""

      Post("/search?relationType=friend", HttpEntity(ContentTypes.`application/json`, inputJson)) ~> routes ~> check {
        handled shouldBe true
        val resp = fromJson(responseAs[String]).extract[List[Person]]
        resp.size shouldEqual 2
        resp.sortBy(_.id) shouldEqual List(person1, person)
      }
    }

  }

}
