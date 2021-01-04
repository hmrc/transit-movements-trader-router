/*
 * Copyright 2021 HM Revenue & Customs
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

package controllers.actions

import base.SpecBase
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class HeaderLoggingActionSpec extends SpecBase with GuiceOneAppPerSuite {

  class Harness(action: HeaderLoggingAction, cc: ControllerComponents) {

    def run(): Action[AnyContent] = (DefaultActionBuilder.apply(cc.parsers.anyContent)(global) andThen action) { result =>
      Results.Ok("")
    }
  }

  "HeaderLoggingAction" - {
    "must pass on request" in {
      def fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("", "")

      val headerLoggingAction = app.injector.instanceOf[HeaderLoggingAction]

      val cc                  = app.injector.instanceOf[ControllerComponents]

      val controller: Harness = new Harness(headerLoggingAction, cc)

      val result: Future[Result] = controller.run()(fakeRequest)

      status(result) mustEqual OK
    }
  }
}
