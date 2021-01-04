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
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class MessageRecipientIdentifierActionSpec extends SpecBase {

  class Harness(action: MessageRecipientIdentifierActionProvider) {

    def run(): Action[AnyContent] = action() { result =>
      Results.Ok("")
    }
  }

  private def actionProvider =
    applicationBuilder
      .build()
      .injector
      .instanceOf[MessageRecipientIdentifierActionProvider]

  private val xMessageRecipient = "MDTP-1-1"

  "MessageSenderIdentifierAction" - {
    "must return an BadRequest when the X-Message-Recipient is missing" in {
      def fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("", "")

      val controller: Harness = new Harness(actionProvider)

      val result: Future[Result] = controller.run()(fakeRequest)

      status(result) mustEqual BAD_REQUEST
    }

    "will process the action when the X-Message-Recipient is present" in {
      def fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
        FakeRequest("", "").withHeaders(
          "X-Message-Recipient" -> xMessageRecipient
        )

      val controller: Harness = new Harness(actionProvider)

      val result: Future[Result] = controller.run()(fakeRequest)

      status(result) mustEqual OK
    }

  }
}
