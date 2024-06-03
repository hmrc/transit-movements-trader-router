/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package filters

import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.Materializer
import base.SpecBase
import org.scalatest.concurrent.ScalaFutures
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers.running

class LoggingFilterSpec extends SpecBase with ScalaFutures {

  implicit private val system: ActorSystem = ActorSystem(suiteName)
  implicit private val mat: Materializer   = Materializer(system)

  "LoggingFilter" - {

    "must pass through a request" in {

      val app = new GuiceApplicationBuilder()
        .configure("microservice.services.features.nctsMonitoringEnabled" -> true)
        .build()

      running(app) {

        val filter  = app.injector.instanceOf[LoggingFilter]
        val builder = app.injector.instanceOf[DefaultActionBuilder]

        val rh: RequestHeader = FakeRequest("GET", "")
        val nextAction: Action[AnyContent] = builder.apply(
          _ => Results.Ok("yay")
        )

        val result = filter.apply(nextAction)(rh).run.futureValue

        result mustEqual Results.Ok("yay")
      }
    }
  }
}
