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

package services

import base.SpecBase
import connectors.{DepartureConnector, DestinationConnector}
import models.MessageType.XMLSubmissionNegativeAcknowledgement
import models.{Directable, MessageRecipient, MessageType}
import org.scalatest.BeforeAndAfterEach
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.mvc.Headers
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.Future

class RoutingServiceSpec extends SpecBase with BeforeAndAfterEach with ScalaCheckPropertyChecks{

  val destinationMessageTypes: Gen[Directable] = Gen.oneOf[Directable](MessageType.arrivalValues)

  val departureMessageTypes: Gen[Directable] = Gen.oneOf[Directable](MessageType.departureValues)

  "sendMessage must" - {
    "use DepartureConnector when forwarding a Departure Message" in {

      forAll(departureMessageTypes) { messageType =>

        val mockDepartureConnector = mock[DepartureConnector]
        val mockDestinationConnector = mock[DestinationConnector]

        when(mockDepartureConnector.sendMessage(any(), any(), any())(any())).thenReturn(Future.successful(HttpResponse(200)))

        val sut = new RoutingService(mockDestinationConnector, mockDepartureConnector)

        sut.sendMessage(MessageRecipient("MDTP-1-1"), messageType, <Abc>123</Abc>, Headers())

        verify(mockDepartureConnector, times(1)).sendMessage(any(), any(), any())(any())
        verify(mockDestinationConnector, times(0)).sendMessage(any(), any(), any())(any())

      }
    }

    "use DestinationConnector when forwarding a Destination Message" in {

      forAll(destinationMessageTypes) { messageType =>
        val mockDepartureConnector = mock[DepartureConnector]
        val mockDestinationConnector = mock[DestinationConnector]

        when(mockDestinationConnector.sendMessage(any(), any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
        val sut = new RoutingService(mockDestinationConnector, mockDepartureConnector)

        sut.sendMessage(MessageRecipient("MDTP-1-1"), messageType, <Abc>123</Abc>, Headers())(HeaderCarrier())

        verify(mockDepartureConnector, times(0)).sendMessage(any(), any(), any())(any())
        verify(mockDestinationConnector, times(1)).sendMessage(any(), any(), any())(any())

      }
    }

    "use DepartureConnector when forwarding a 917 message with DEP header" in {

      val messageType = XMLSubmissionNegativeAcknowledgement
        val mockDepartureConnector = mock[DepartureConnector]
        val mockDestinationConnector = mock[DestinationConnector]

        when(mockDestinationConnector.sendMessage(any(), any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
        val sut = new RoutingService(mockDestinationConnector, mockDepartureConnector)

        sut.sendMessage(MessageRecipient("MDTP-DEP-1-1"), messageType, <Abc>123</Abc>, Headers())(HeaderCarrier())

        verify(mockDepartureConnector, times(1)).sendMessage(any(), any(), any())(any())
        verify(mockDestinationConnector, times(0)).sendMessage(any(), any(), any())(any())

    }

    "use DestinationConnector when forwarding a 917 message with an ARR header" in {

      val messageType = XMLSubmissionNegativeAcknowledgement
      val mockDepartureConnector = mock[DepartureConnector]
      val mockDestinationConnector = mock[DestinationConnector]

      when(mockDestinationConnector.sendMessage(any(), any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
      val sut = new RoutingService(mockDestinationConnector, mockDepartureConnector)

      sut.sendMessage(MessageRecipient("MDTP-ARR-1-1"), messageType, <Abc>123</Abc>, Headers())(HeaderCarrier())

      verify(mockDepartureConnector, times(0)).sendMessage(any(), any(), any())(any())
      verify(mockDestinationConnector, times(1)).sendMessage(any(), any(), any())(any())

    }

    "use DestinationConnector when forwarding a 917 message without ARR or DEP in header" in {

      val messageType = XMLSubmissionNegativeAcknowledgement
      val mockDepartureConnector = mock[DepartureConnector]
      val mockDestinationConnector = mock[DestinationConnector]

      when(mockDestinationConnector.sendMessage(any(), any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
      val sut = new RoutingService(mockDestinationConnector, mockDepartureConnector)

      sut.sendMessage(MessageRecipient("MDTP-1-1"), messageType, <Abc>123</Abc>, Headers())(HeaderCarrier())

      verify(mockDepartureConnector, times(0)).sendMessage(any(), any(), any())(any())
      verify(mockDestinationConnector, times(1)).sendMessage(any(), any(), any())(any())

    }
  }
}
