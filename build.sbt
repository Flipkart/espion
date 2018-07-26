
organization := "com.flipkart"

name := "espion"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.11.7",
  "io.dropwizard.metrics5" % "metrics-core" % "5.0.0"
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))


bintrayRepository := "maven"

bintrayOrganization := None