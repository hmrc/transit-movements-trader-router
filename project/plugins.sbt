resolvers += "HMRC-open-artefacts-maven" at "https://open.artefacts.tax.service.gov.uk/maven2"

resolvers += Resolver.url("HMRC-open-artefacts-ivy", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)

resolvers += Resolver.typesafeRepo("releases")

addSbtPlugin("uk.gov.hmrc"               % "sbt-auto-build"       % "3.21.0")
addSbtPlugin("uk.gov.hmrc"               % "sbt-distributables"   % "2.5.0")
addSbtPlugin("org.playframework"         % "sbt-plugin"           % "3.0.1")
addSbtPlugin("org.scoverage"             % "sbt-scoverage"        % "2.0.9")
addSbtPlugin("org.scalameta"             % "sbt-scalafmt"         % "2.5.2")
addSbtPlugin("ch.epfl.scala"             % "sbt-scalafix"         % "0.11.1")
addSbtPlugin("com.timushev.sbt"          % "sbt-updates"          % "0.6.0")
addSbtPlugin("net.virtual-void"          % "sbt-dependency-graph" % "0.10.0-RC1")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat"         % "0.1.20")
