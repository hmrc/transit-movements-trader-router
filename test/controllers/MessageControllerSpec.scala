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

package controllers

import base.SpecBase
import org.mockito.Mockito.reset
import org.mockito.Mockito.when
import org.mockito.ArgumentMatchers._
import org.scalatest.BeforeAndAfterEach
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.RoutingService
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.Future

class MessageControllerSpec extends SpecBase with BeforeAndAfterEach {

  private val mockRoutingService = mock[RoutingService]

  private val application = applicationBuilder
    .overrides(bind[RoutingService].toInstance(mockRoutingService))
    .build()

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockRoutingService)
  }

  private val xMessageRecipient = "MDTP-1-1"
  private val xMessageType      = "IE008"

  "MessageController must" - {
    "return Ok when X-Message-Recipient and X-Message-Type are defined and there is an Ok with Location header from downstream response" in {
      when(mockRoutingService.sendMessage(any(), any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(OK, body = null, Map(LOCATION -> Seq("/movements/arrivals/1/messages/1")))))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Recipient" -> xMessageRecipient,
            "X-Message-Type"      -> xMessageType,
            "Content-Type"        -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe OK
      contentAsString(result) mustBe empty
      header(LOCATION, result) mustBe Some("/movements/arrivals/1/messages/1")
    }

    "return Ok when X-Message-Recipient and X-Message-Type are defined and there is an Ok with no Location header from downstream response" in {
      when(mockRoutingService.sendMessage(any(), any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(OK, null)))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Recipient" -> xMessageRecipient,
            "X-Message-Type"      -> xMessageType,
            "Content-Type"        -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe OK
      contentAsString(result) mustBe empty
      header(LOCATION, result) mustBe None
    }

    "return BadRequest when X-Message-Recipient is not defined" in {
      when(mockRoutingService.sendMessage(any(), any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(OK, null)))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders("Content-Type" -> "application/xml", "X-Message-Type" -> xMessageType)
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe empty
    }

    "return BadRequest when X-Message-Type is not defined" in {
      when(mockRoutingService.sendMessage(any(), any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(OK, null)))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders("Content-Type" -> "application/xml", "X-Message-Recipient" -> xMessageRecipient)
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe "BadRequest: missing header key X-Message-Type"
    }

    "return a Bad Request when upstream returns a Bad Request" in {
      when(mockRoutingService.sendMessage(any(), any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(BAD_REQUEST, null)))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Recipient" -> xMessageRecipient,
            "X-Message-Type"      -> xMessageType,
            "Content-Type"        -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe BAD_REQUEST
    }

    "return a Bad Request with response body when upstream returns a Bad Request with response body" in {
      when(mockRoutingService.sendMessage(any(), any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(BAD_REQUEST, "message")))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Recipient" -> xMessageRecipient,
            "X-Message-Type"      -> xMessageType,
            "Content-Type"        -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe BAD_REQUEST
      contentAsString(result).endsWith("message") mustBe true
    }

    "return a Not Found when upstream returns a Not Found" in {
      when(mockRoutingService.sendMessage(any(), any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(NOT_FOUND, null)))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Recipient" -> xMessageRecipient,
            "X-Message-Type"      -> xMessageType,
            "Content-Type"        -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe NOT_FOUND
      contentAsString(result) mustBe empty
    }

    "return a Locked when upstream returns a Locked" in {
      when(mockRoutingService.sendMessage(any(), any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(LOCKED, null)))

      val request =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Recipient" -> xMessageRecipient,
            "X-Message-Type"      -> xMessageType,
            "Content-Type"        -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value

      status(result) mustBe LOCKED
      contentAsString(result) mustBe empty
    }

    "return Internal Server Error when upstream returns an Internal Server Error" in {
      when(mockRoutingService.sendMessage(any(), any(), any(), any())(any()))
        .thenReturn(Future.successful(HttpResponse(INTERNAL_SERVER_ERROR, null)))

      val fakeRequest =
        FakeRequest("POST", routes.MessageController.handleMessage().url)
          .withHeaders(
            "X-Message-Recipient" -> xMessageRecipient,
            "X-Message-Type"      -> xMessageType,
            "Content-Type"        -> "application/xml"
          )
          .withXmlBody(<xml>test</xml>)

      val result = route(application, fakeRequest).value

      status(result) mustBe INTERNAL_SERVER_ERROR
      contentAsString(result) mustBe empty
    }
  }
}
