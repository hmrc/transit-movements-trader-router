import uk.gov.hmrc.DefaultBuildSettings
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin

val appName = "transit-movements-trader-router"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(inThisBuild(buildSettings))
  .settings(inConfig(Test)(testSettings))
  .settings(scalacSettings)
  .settings(ScoverageSettings())
  .settings(
    majorVersion := 0,
    scalaVersion := "2.13.12",
    PlayKeys.playDefaultPort := 9486,
    resolvers += Resolver.jcenterRepo,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test
  )

lazy val scalacSettings = Def.settings(
  // Disable warnings arising from generated routing code
  scalacOptions += "-Wconf:src=routes/.*:silent",
  // Disable fatal warnings and warnings from discarding values
  scalacOptions ~= {
    opts =>
      opts.filterNot(Set("-Xfatal-warnings", "-Ywarn-value-discard"))
  },
  // Disable dead code warning as it is triggered by Mockito any()
  Test / scalacOptions ~= {
    opts =>
      opts.filterNot(Set("-Ywarn-dead-code"))
  }
)

lazy val buildSettings = Def.settings(
  useSuperShell := false,
  scalafixDependencies ++= Seq(
    "com.github.liancheng" %% "organize-imports" % "0.5.0"
  )
)

lazy val testSettings = Def.settings(
  fork := false,
  javaOptions ++= Seq(
    "-Dlogger.resource=logback-test.xml"
  )
)
