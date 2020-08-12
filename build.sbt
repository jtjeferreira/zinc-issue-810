ThisBuild / scalaVersion     := "2.13.2"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"
ThisBuild / logLevel         := Level.Debug

lazy val root = (project in file("."))
  .settings(
    name := "scala-seed-zinc-problem",
  ).dependsOn(core).aggregate(core)

lazy val core = (project in file("core"))
  .settings(
    name := "core",
  )

