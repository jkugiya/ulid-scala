// For benchmark
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.4.2")
// sonatype
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.17")
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.1.2")
// coverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.1.1")
addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.3.13")

// Cross-compilation
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.3.2")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.3.2")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.16.0")
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.5.4")