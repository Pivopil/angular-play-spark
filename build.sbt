name := "play"

version := "1.0"

lazy val `play` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(jdbc, anorm, cache, ws,
  "mysql" % "mysql-connector-java" % "5.1.35",
  "com.typesafe.play" %% "play-slick" % "0.8.0",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "io.strongtyped" %% "active-slick" % "0.2.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.apache.spark" % "spark-core_2.11" % "1.4.0",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.3.12",
  "com.typesafe.akka" % "akka-slf4j_2.11" % "2.3.12",
  "org.apache.spark" % "spark-streaming_2.11" % "1.4.0",
  "org.apache.spark" % "spark-sql_2.11" % "1.4.0",
  "org.apache.spark" % "spark-mllib_2.11" % "1.4.0")

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")