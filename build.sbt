name := "mmon"

version := "0.1.0"

scalaVersion := "2.12.10"

// resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
// resolvers += "mvnrepository" at "http://mvnrepository.com/artifact/"

// libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala" % "2.+"

val circeVersion = "0.12.+"
libraryDependencies ++= Seq(
  "io.circe"  %% "circe-core"     % circeVersion,
  "io.circe"  %% "circe-generic"  % circeVersion,
  "io.circe"  %% "circe-parser"   % circeVersion
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"