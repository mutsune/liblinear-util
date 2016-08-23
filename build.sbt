name := "liblinear-util"

version := "1.0"

scalaVersion := "2.11.8"

lazy val root = project.in(file(".")).dependsOn(scalaUtilsRepo)

lazy val scalaUtilsRepo = uri("git://github.com/mutsune/scala-utils.git#1.0")

libraryDependencies ++= Seq(
    "de.bwaldvogel" % "liblinear" % "1.95" withSources() withJavadoc()
)
