# Akka Http JanusGraph
This is a simple sbt project using showcasing the use of <a href = "http://janusgraph.org">JanusGraph database </a> using aka-http as a toolkit for RestAPI.

JanusGraph is a scalable graph database used for storing and querying graphs containing millions\ of vertices and edges which are distributed across a multi-machine cluster.

To run unit test cases: 

```$ sbt test```

Now, to run the app:
Before running this app, make sure that you have <a href = "http://cassandra.apache.org/download">Cassandra (version 2.1.17) </a> running on your system.


```$ sbt 'project root' 'run-main com.graph.api.GraphApi```
