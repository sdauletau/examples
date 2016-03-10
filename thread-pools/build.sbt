name := "thread-pools"

version := "0.0.1"

scalaVersion := "2.11.7"

dependencyOverrides += "org.scala-lang" % "scala-library" % scalaVersion.value

val commonDependencies = {
  val akkaVersion = "2.4.1"
  val akkaStreamVersion = "2.0.3"
  Seq(
    //"com.typesafe.akka" % "akka-cluster_2.11" % akkaVersion,
    //"com.typesafe.akka" % "akka-remote_2.11" % akkaVersion,
    //"com.typesafe.akka" % "akka-slf4j_2.11" % akkaVersion,
    //"com.typesafe.akka" % "akka-agent_2.11" % akkaVersion,
    //"com.typesafe.akka" % "akka-http-experimental_2.11" % akkaStreamVersion,
    //"com.typesafe.akka" % "akka-stream-experimental_2.11" % akkaStreamVersion,
    //"com.typesafe.akka" % "akka-http-core-experimental_2.11" % akkaStreamVersion,
    "com.typesafe.akka" % "akka-actor_2.11" % akkaVersion
  )
}

libraryDependencies ++= commonDependencies

scalacOptions ++= Seq("-feature", "-deprecation")