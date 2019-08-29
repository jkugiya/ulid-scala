import Dependencies._

ThisBuild / scalaVersion     := "2.12.9"
ThisBuild / crossScalaVersions := Seq("2.13.0", "2.12.9")
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "jkugiya"
ThisBuild / organizationName := "jkugiya"

lazy val root = (project in file("."))
  .settings(
    name := "ulid-scala",
    libraryDependencies += scalaTest % Test
  )

// Uncomment the following for publishing to Sonatype.
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for more detail.

 ThisBuild / description := "A Scala port of alizain/ulid"
 ThisBuild / licenses    := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
 ThisBuild / homepage    := Some(url("https://github.com/jkugiya/ulid-scala"))
 ThisBuild / scmInfo := Some(
   ScmInfo(
     url("https://github.com/jkugiya/ulid-scala"),
     "scm:git@github.com:jkugiya/ulid-scala.git"
   )
 )
 ThisBuild / developers := List(
   Developer(
     id    = "jkugiya",
     name  = "Jiro kugiya",
     email = "j.kugiya@gmail.com",
     url   = url("https://github.com/jkugiya")
   )
 )
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
   val nexus = "https://oss.sonatype.org/"
   if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
   else Some("releases" at nexus + "service/local/staging/deploy/maven2")
 }
ThisBuild / publishMavenStyle := true
