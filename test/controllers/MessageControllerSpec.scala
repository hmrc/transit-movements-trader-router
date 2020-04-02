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
import play.api.test.Helpers._
import play.api.test.FakeRequest
import org.mockito.Matchers.any
import org.mockito.Mockito.{reset, when}
import connector.DestinationConnector
import org.scalatest.BeforeAndAfterEach
import play.api.inject.bind
import uk.gov.hmrc.http.{BadRequestException, HttpResponse, NotFoundException}

import scala.concurrent.Future

class MessageControllerSpec extends SpecBase with BeforeAndAfterEach {

  private val mockConnector = mock[DestinationConnector]
  private val application = applicationBuilder.overrides(bind[DestinationConnector].toInstance(mockConnector)).build()

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockConnector)
  }

  "MessageController must" - {
    "return ok for the message Type Goods release" in {
      when(mockConnector.sendMessage(any(), any())(any(), any())).thenReturn(Future.successful(HttpResponse(OK)))

      val request = FakeRequest("POST", routes.MessageController.handleMessageType().url)
        .withHeaders(("X-Message-Type", "IE025"),("X-Message-Sender", "MDTP-1-1"))
        .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value
      status(result) mustBe OK
    }

    "return not found when service returns not found exception" in {
      when(mockConnector.sendMessage(any(), any())(any(), any())).thenReturn(Future.failed(new NotFoundException("not found")))

      val request = FakeRequest("POST", routes.MessageController.handleMessageType().url)
        .withHeaders(("X-Message-Type", "IE025"),("X-Message-Sender", "MDTP-1-1"))
        .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value
      status(result) mustBe NOT_FOUND
    }

    "return a bad request when service returns Bad request exception" in {
      when(mockConnector.sendMessage(any(), any())(any(), any())).thenReturn(Future.failed(new BadRequestException("bad request")))

      val request = FakeRequest("POST", routes.MessageController.handleMessageType().url)
        .withHeaders(("X-Message-Type", "IE025"),("X-Message-Sender", "MDTP-1-1"))
        .withXmlBody(<xml>test</xml>)

      val result = route(application, request).value
      status(result) mustBe BAD_REQUEST
    }

    "return a bad request when request has no header" in {
      when(mockConnector.sendMessage(any(), any())(any(), any())).thenReturn(Future.successful(HttpResponse(OK)))

      val fakeRequest = FakeRequest("POST", routes.MessageController.handleMessageType().url)
        .withXmlBody(<xml>test</xml>)

      val result = route(application, fakeRequest).value
      status(result) mustBe BAD_REQUEST
    }

    "return Not acceptable status when message type is not valid" in {
      when(mockConnector.sendMessage(any(), any())(any(), any())).thenReturn(Future.successful(HttpResponse(OK)))

      val fakeRequest = FakeRequest("POST", routes.MessageController.handleMessageType().url).withHeaders(("X-Message-Type", "XYD"))
        .withXmlBody(<xml>test</xml>)

      val result = route(application, fakeRequest).value
      status(result) mustBe NOT_ACCEPTABLE
    }
  }
}
