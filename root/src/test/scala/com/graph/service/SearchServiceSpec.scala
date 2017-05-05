package com.graph.service

import com.graph.drivers.{JanusInMemoryDB, JanusInMemoryGraphFactory}
import com.graph.models.Person
import org.scalatest.{BeforeAndAfterAll, WordSpec}
import gremlin.scala._

class SearchServiceSpec extends WordSpec with BeforeAndAfterAll with JanusInMemoryGraphFactory {

  override def beforeAll = {
    JanusInMemoryDB.janusGraph.openManagement()
  }

  object SearchServiceTest extends SearchService with JanusInMemoryGraphFactory

  "SearchService" should {
    "search by name" in {
      withTransaction { implicit graph =>
        graph + Person(id = None, name = "Harshit", `type` = "employee", age = 25)
      }
      val resp = SearchServiceTest.searchByName("Harshit")
      assert(resp.size == 1)
    }

    "search by id" in {
      val id = withTransaction { implicit graph =>
        val personV = graph + Person(id = None, name = "harshit", `type` = "employee", age = 25)
        personV.id.toString
      }
      val resp = SearchServiceTest.searchById(id)
      assert(resp.size == 1)
    }

    "search by person type" in {
      withTransaction { implicit graph =>
        graph + Person(id = None, name = "harshit", `type` = "shopkeeper", age = 27)
      }
      val resp = SearchServiceTest.searchByType("shopkeeper")
      assert(resp.size == 1)
    }

    "find relations for a Person" in {
      val person1 = Person(id = None, name = "harshit", `type` = "employee", age = 25)
      val person2 = Person(id = None, name = "gaurav", `type` = "employee", age = 26)
      withTransaction { implicit graph =>
        val person1V = graph + person1
        val person2V = graph + person2

        person1V --- "friend" --> person2V
      }
      val resp = SearchServiceTest.findRelation("friend", person1)
      assert(resp.size == 1)
      assert(resp.head.name == person2.name)
    }

  }
}
