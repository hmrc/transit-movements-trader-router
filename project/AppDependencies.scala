import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-28" % "5.16.0"
  )

  val test = Seq(
    "org.scalatestplus"      %% "scalacheck-1-15"     % "3.2.9.0",
    "org.scalatestplus"      %% "mockito-3-2"         % "3.1.2.0",
    "com.github.tomakehurst"  % "wiremock-standalone" % "2.25.1",
    "com.vladsch.flexmark"    % "flexmark-all"        % "0.36.8"
  ).map(_ % s"$Test, $IntegrationTest")
}
