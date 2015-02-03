name := """ALKSA-eawa"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

resolvers += (
    "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository"
)

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "ALKSA" % "ALKSA" % "0.0.1-SNAPSHOT"
)

LessKeys.compress := true

