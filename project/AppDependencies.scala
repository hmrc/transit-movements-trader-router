import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-30" % "8.4.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30" % "8.4.0",
    "org.scalatestplus" %% "scalacheck-1-15" % "3.2.9.0",
    "org.scalatestplus" %% "mockito-3-2" % "3.1.2.0",
    "org.mockito" % "mockito-core" % "3.9.0"
  ).map(_ % s"$Test")
}
