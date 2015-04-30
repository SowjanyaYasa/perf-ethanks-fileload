name := """perf-ethanks-fileload"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.apache.poi" % "poi" % "3.11",
  "org.apache.poi" % "poi-ooxml" % "3.11",
  "org.apache.poi" % "poi-ooxml-schemas" % "3.11",
  "org.apache.poi" % "poi-ooxml" % "3.9",
  "com.google.inject" % "guice" % "3.0",
  "net.codingwell" % "scala-guice_2.11" % "4.0.0-beta4",
  "com.google.guava" % "guava" % "12.0",
  "com.rabbitmq" % "amqp-client" % "3.3.4"
)


