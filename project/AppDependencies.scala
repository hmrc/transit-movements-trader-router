import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-28" % "5.12.0"
  )

  val test = Seq(
    "org.scalatest"          %% "scalatest"           % "3.2.9",
    "com.typesafe.play"      %% "play-test"           % PlayVersion.current,
    "org.scalatestplus"      %% "scalacheck-1-15"     % "3.2.9.0",
    "org.scalatestplus.play" %% "scalatestplus-play"  % "4.0.3",
    "org.scalatestplus"      %% "mockito-3-2"         % "3.1.2.0",
    "org.mockito"             % "mockito-core"        % "3.9.0",
    "com.github.tomakehurst"  % "wiremock-standalone" % "2.25.1",
    "com.vladsch.flexmark"    % "flexmark-all"        % "0.36.8"
  ).map(_ % s"$Test, $IntegrationTest")
}
