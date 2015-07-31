name := "dummy"

organization := "dummy"

version := "1.0"

scalaVersion := "2.11.6"


resolvers += "AkkaRepository" at "http://repo.akka.io/releases/"

libraryDependencies ++= Seq(
  "io.netty"  % "netty-all" % "4.0.9.Final",
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-remote" % "2.3.11",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "com.googlecode.scalascriptengine" %% "scalascriptengine" % "1.3.10",
  "org.scala-lang" % "scala-compiler" % "2.11.6",
  "org.slf4j" % "slf4j-simple" % "1.6.4",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
)
