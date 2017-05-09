package com.graph.service

import com.graph.drivers.{JanusInMemoryDB, JanusInMemoryGraphFactory}
import com.graph.models.{Person, Relation}
import org.scalatest.{BeforeAndAfterAll, WordSpec}


class RelationsSpec extends WordSpec with BeforeAndAfterAll with JanusInMemoryGraphFactory {

  override def beforeAll = {
    JanusInMemoryDB.janusGraph.openManagement()
  }


  object RelationServiceTest extends Relations with JanusInMemoryGraphFactory

  "RelationService" should {
    "insert a relation between two person" in {
      val person1 = Person(id = None, name = "harshit", `type` = "employee", age = 25)
      val person2 = Person(id = None, name = "gaurav", `type` = "employee", age = 26)
      val relation = Relation(person1, person2, "colleague")
      val resp = RelationServiceTest.insert(relation)

      assert(resp.from.id.nonEmpty)
      assert(resp.to.id.nonEmpty)
    }

  }

}
