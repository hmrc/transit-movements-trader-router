/*
 * Copyright 2020 HM Revenue & Customs
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

package controllers

import base.SpecBase
import connectors.DestinationConnector
import org.mockito.Matchers.any
import org.mockito.Mockito.{reset, when}
import org.scalatest.BeforeAndAfterEach
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.Future

class MessageControllerSpec extends SpecBase with BeforeAndAfterEach {

  private val mockConnector = mock[DestinationConnector]
  private val application = applicationBuilder
    .overrides(bind[DestinationConnector].toInstance(mockConnector))
    .build()

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockConnector)
  }

  private val xMessageSender = "MDTP-1-1"

  "MessageController must" - {
    "return Ok when X-Message-Sender is defined and there is an Ok from upstream" in {
      when(mockConnector.sendMessage(any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(OK)))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Sender" -> xMessageSender,
            "Content-Type" -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe OK
    }

    "return a Bad Request when upstream returns a Bad Request" in {
      when(mockConnector.sendMessage(any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(BAD_REQUEST)))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Sender" -> xMessageSender,
            "Content-Type" -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe BAD_REQUEST
    }

    "return a Not Found when upstream returns a Not Found" in {
      when(mockConnector.sendMessage(any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(NOT_FOUND)))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Sender" -> xMessageSender,
            "Content-Type" -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe NOT_FOUND
    }

    "return a Locked when upstream returns a Locked" in {
      when(mockConnector.sendMessage(any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(LOCKED)))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Sender" -> xMessageSender,
            "Content-Type" -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe LOCKED
    }

    "return Internal Server Error when upstream returns an Internal Server Error" in {
      when(mockConnector.sendMessage(any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(INTERNAL_SERVER_ERROR)))

      val fakeRequest =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Sender" -> xMessageSender,
            "Content-Type" -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, fakeRequest).value

      status(result) mustBe INTERNAL_SERVER_ERROR
    }
  }
}
