name := "akka-http-JanusGraph"

version := "1.0"

scalaVersion in ThisBuild := "2.11.8"


lazy val root = project.in(file("root")).settings(
  libraryDependencies := (janusCassandra.value ++ janusCore.value ++ akkaHttp.value ++ gremlinScala.value ++
    scalaTest.value ++ akkaHttpTestkit.value ++ mockito.value ++ json4sNative.value),

  parallelExecution in Test := false,
  resolvers += Resolver.url("sonatype-nexus-snapshots", url("https://oss.sonatype.org/content/repositories/snapshots")),
  resolvers += "jitpack" at "https://jitpack.io"
)

val akkaVersion = "10.0.5"


def janusCassandra = Def.setting {
  "org.janusgraph" % "janusgraph-cassandra" % "0.1.0" :: Nil
}

def gremlinScala = Def.setting {
  "com.michaelpollmeier" %% "gremlin-scala" % "3.2.4.9" :: Nil
}

def janusCore = Def.setting {
  "org.janusgraph" % "janusgraph" % "0.1.0" :: Nil
}

def akkaHttp = Def.setting {
  "com.typesafe.akka" %% "akka-http" % akkaVersion :: Nil
}

def scalaTest = Def.setting {
  "org.scalatest" %% "scalatest" % "3.0.1" % "test" :: Nil
}

def akkaHttpTestkit = Def.setting {
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test" :: Nil
}

def mockito = Def.setting {
  "org.mockito" % "mockito-core" % "1.10.19" % "test" :: Nil
}

def json4sNative = Def.setting { "org.json4s" %% "json4s-native" % "3.5.0" :: Nil }
