import Dependencies._

ThisBuild / scalaVersion     := "2.13.4"
ThisBuild / crossScalaVersions := Seq("2.13.4", "2.12.13", "2.11.12")
ThisBuild / version          := "1.0.2-SNAPSHOT"
ThisBuild / organization     := "com.github.jkugiya"
ThisBuild / organizationName := "jkugiya"

lazy val root = (project in file("."))
  .settings(
    name := "ulid-scala",
    libraryDependencies += scalaTest % Test
  )

lazy val benchmark = (project in file("benchmark"))
  .enablePlugins(JmhPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "de.huxhorn.sulky" % "de.huxhorn.sulky.ulid" % "8.2.0",
      "io.azam.ulidj" % "ulidj" % "1.0.0"
    )
  )
  .dependsOn(root)

// Uncomment the following for publishing to Sonatype.
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for more detail.

 ThisBuild / description := "A Scala port of alizain/ulid"
 ThisBuild / licenses    := List("MIT" -> new URL("https://github.com/jkugiya/ulid-scala/blob/master/LICENSE"))
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
