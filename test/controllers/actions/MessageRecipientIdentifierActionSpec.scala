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

package controllers.actions

import base.SpecBase
import models.MessageRecipient
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.mvc._
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Future

class MessageRecipientIdentifierActionSpec extends SpecBase with ScalaCheckPropertyChecks {

  class Harness(action: MessageRecipientIdentifierActionProvider) {

    def run(): Action[AnyContent] = action() {
      _ =>
        Results.Ok("")
    }
  }

  private def actionProvider =
    applicationBuilder
      .build()
      .injector
      .instanceOf[MessageRecipientIdentifierActionProvider]

  val arrivalRecipient   = MessageRecipient.fromHeaderValue("MDTP-ARR-1-1")
  val departureRecipient = MessageRecipient.fromHeaderValue("MDTP-DEP-1-1")
  val guaranteeRecipient = MessageRecipient.fromHeaderValue("MDTP-GUA-1-1")
  val messageRecipients  = Gen.oneOf(Seq(arrivalRecipient, departureRecipient, guaranteeRecipient))

  "MessageSenderIdentifierAction" - {
    "must return a BadRequest when the X-Message-Recipient is missing" in {
      def fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("", "")

      val controller: Harness = new Harness(actionProvider)

      val result: Future[Result] = controller.run()(fakeRequest)

      status(result) mustEqual BAD_REQUEST
    }

    "must return a BadRequest when the X-Message-Recipient is invalid" in {
      def fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
        FakeRequest("", "").withHeaders(
          "X-Message-Recipient" -> "MDTP-FOO-1-1"
        )

      val controller: Harness = new Harness(actionProvider)

      val result: Future[Result] = controller.run()(fakeRequest)

      status(result) mustEqual BAD_REQUEST
    }

    "will process the action when a valid X-Message-Recipient is present" in forAll(messageRecipients) {
      messageRecipient =>
        def fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
          FakeRequest("", "").withHeaders(
            "X-Message-Recipient" -> messageRecipient.get.headerValue
          )

        val controller: Harness = new Harness(actionProvider)

        val result: Future[Result] = controller.run()(fakeRequest)

        status(result) mustEqual OK
    }

  }
}
