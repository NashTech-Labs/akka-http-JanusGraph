package com.graph.drivers

import gremlin.scala.{ScalaGraph, _}
import org.janusgraph.core.JanusGraph

import scala.util.{Failure, Success, Try}

trait GraphFactory {

  val janusGraph: JanusGraph

  def withTransaction[T](func: ScalaGraph => T): T = {
    val transaction = janusGraph.newTransaction
    Try {
      val result = func(transaction.asScala)
      transaction.commit()
      result
    } match {
      case Success(resp) => if (!transaction.isClosed) transaction.close()
        resp
      case Failure(exc) => println("Some exception triggered, trying to rollback...")
        if (!transaction.isClosed) transaction.rollback()
        throw exc
    }
  }

}
