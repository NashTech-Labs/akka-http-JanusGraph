package com.graph.drivers

import org.apache.commons.configuration.BaseConfiguration
import org.janusgraph.core.JanusGraphFactory


object JanusConfig {

  private val conf = new BaseConfiguration

  conf.setProperty("storage.backend", "cassandra")
  conf.setProperty("storage.hostname", "localhost")
  conf.setProperty("storage.cassandra.keyspace", "janus")

  val janusGraph = JanusGraphFactory.open(conf)

}

trait JanusGraphFactory extends GraphFactory {
  lazy val janusGraph = JanusConfig.janusGraph
}

trait JanusInMemoryGraphFactory extends GraphFactory {
  lazy val janusGraph = JanusInMemoryDB.janusGraph
}

object JanusInMemoryDB {

  private val conf = new BaseConfiguration

  conf.setProperty("storage.backend", "inmemory")

  val janusGraph = JanusGraphFactory.open(conf)

}