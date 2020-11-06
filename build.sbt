name := "sample-functional-ddd"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= {
  val akkaVersion = "2.6.10"
  val akkaHttpVersion = "10.2.1"
  val catsVersion = "2.2.0"
  val guiceVersion = "4.2.11"
  val slickVersion = "3.3.2"
  val mysqlVersion = "8.0.11"
  val log4jVersion = "1.7.2"
  Seq(
    "org.slf4j" % "slf4j-log4j12" % log4jVersion,
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-effect" % catsVersion,
    "net.codingwell" %% "scala-guice" % guiceVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.slick" %% "slick" % slickVersion,
    "mysql" % "mysql-connector-java" % mysqlVersion
  )
}

scalacOptions ++= Seq(
  "-Xfatal-warnings",
  "-language:postfixOps",
  "-deprecation"
)