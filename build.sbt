enablePlugins(PlayScala)

scalaVersion := "2.13.7"

libraryDependencies ++= Seq(
  jdbc,
  "org.playframework.anorm" %% "anorm" % "2.6.10",
  "org.postgresql" % "postgresql" % "42.3.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.39.12" % Test,
  "com.dimafeng" %% "testcontainers-scala-postgresql" % "0.39.12" % Test
)
