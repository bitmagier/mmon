name := "mmon"

version := "0.3.9"

scalaVersion := "2.12.10"

// resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
// resolvers += "mvnrepository" at "http://mvnrepository.com/artifact/"

val circeVersion = "0.12.+"
libraryDependencies += "io.circe" %% "circe-core" % circeVersion
libraryDependencies += "io.circe" %% "circe-generic" % circeVersion
libraryDependencies += "io.circe" %% "circe-parser" % circeVersion

libraryDependencies += "com.typesafe" % "config" % "1.4.+"
libraryDependencies += "com.paulgoldbaum" %% "scala-influxdb-client" % "[0.6,)"

val log4jVersion = "2.12.1"
libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % log4jVersion
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % log4jVersion
libraryDependencies += "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.+" % "test"