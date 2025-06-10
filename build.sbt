ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.4"

val dependencies = Seq(
  // ZIO HTTP
  "dev.zio" %% "zio-http" % "3.3.3",
  "io.github.iltotore" %% "iron" % "3.0.1",
  "io.getquill" %% "quill-jdbc-zio" % "4.8.6",
  "org.postgresql" % "postgresql" % "42.7.6",
  "dev.zio" %% "zio-prelude" % "1.0.0-RC41",
  "dev.zio" %% "zio-test" % "2.1.19" % Test,
  "dev.zio" %% "zio-http-testkit" % "3.3.3" % Test,
  "dev.zio" %% "zio-test-sbt" % "2.1.19" % Test,
  "org.testcontainers" % "testcontainers" % "1.21.1",
  "org.testcontainers" % "postgresql" % "1.21.1",
)

lazy val root = (project in file("."))
  .settings(
    name := "App",
    libraryDependencies ++= dependencies
  )
  .dependsOn(zioWithIron)

lazy val zioWithIron = (project in file("modules/ZIOWithIron"))
  .settings(
    libraryDependencies ++= dependencies
  )
