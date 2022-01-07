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

package services

import base.SpecBase
import connectors.DepartureConnector
import connectors.DestinationConnector
import connectors.GuaranteeConnector
import models.MessageRecipient
import models.MessageType
import models.MessageType.XMLSubmissionNegativeAcknowledgement
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalacheck.Gen
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpResponse

import scala.concurrent.Future

class RoutingServiceSpec extends SpecBase with BeforeAndAfterEach with ScalaCheckPropertyChecks {

  val destinationMessageTypes: Gen[MessageType] = Gen.oneOf(MessageType.arrivalValues)
  val departureMessageTypes: Gen[MessageType]   = Gen.oneOf(MessageType.departureValues)
  val guaranteeMessageTypes: Gen[MessageType]   = Gen.oneOf(MessageType.guaranteeValues)

  "sendMessage must" - {
    "use DepartureConnector when forwarding a Departure Message" in {

      forAll(departureMessageTypes) {
        messageType =>
          val mockDepartureConnector   = mock[DepartureConnector]
          val mockDestinationConnector = mock[DestinationConnector]
          val mockGuaranteeConnector   = mock[GuaranteeConnector]
          val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-DEP-1-1").get

          when(mockDepartureConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))

          val sut = new RoutingService(mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector)

          sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)

          verify(mockDepartureConnector, times(1)).sendMessage(any(), any())(any())
          verify(mockDestinationConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockGuaranteeConnector, times(0)).sendMessage(any(), any())(any())
      }
    }

    "use DestinationConnector when forwarding a Destination Message" in {
      forAll(destinationMessageTypes) {
        messageType =>
          val mockDepartureConnector   = mock[DepartureConnector]
          val mockDestinationConnector = mock[DestinationConnector]
          val mockGuaranteeConnector   = mock[GuaranteeConnector]
          val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-ARR-1-1").get

          when(mockDestinationConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
          val sut = new RoutingService(mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector)

          sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)(HeaderCarrier())

          verify(mockDepartureConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockDestinationConnector, times(1)).sendMessage(any(), any())(any())
          verify(mockGuaranteeConnector, times(0)).sendMessage(any(), any())(any())
      }
    }

    "use GuaranteeConnector when forwarding a Guarantee Message" in {
      forAll(guaranteeMessageTypes) {
        messageType =>
          val mockDepartureConnector   = mock[DepartureConnector]
          val mockDestinationConnector = mock[DestinationConnector]
          val mockGuaranteeConnector   = mock[GuaranteeConnector]
          val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-GUA-1-1").get

          when(mockDestinationConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
          val sut = new RoutingService(mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector)

          sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)(HeaderCarrier())

          verify(mockDepartureConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockDestinationConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockGuaranteeConnector, times(1)).sendMessage(any(), any())(any())
      }
    }

    "use DepartureConnector when forwarding a 917 message with DEP header" in {
      val messageType              = XMLSubmissionNegativeAcknowledgement
      val mockDepartureConnector   = mock[DepartureConnector]
      val mockDestinationConnector = mock[DestinationConnector]
      val mockGuaranteeConnector   = mock[GuaranteeConnector]
      val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-DEP-1-1").get

      when(mockDestinationConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
      val sut = new RoutingService(mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector)

      sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)(HeaderCarrier())

      verify(mockDepartureConnector, times(1)).sendMessage(any(), any())(any())
      verify(mockDestinationConnector, times(0)).sendMessage(any(), any())(any())
      verify(mockGuaranteeConnector, times(0)).sendMessage(any(), any())(any())
    }

    "use DestinationConnector when forwarding a 917 message with an ARR header" in {
      val messageType              = XMLSubmissionNegativeAcknowledgement
      val mockDepartureConnector   = mock[DepartureConnector]
      val mockDestinationConnector = mock[DestinationConnector]
      val mockGuaranteeConnector   = mock[GuaranteeConnector]
      val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-ARR-1-1").get

      when(mockDestinationConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
      val sut = new RoutingService(mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector)

      sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)(HeaderCarrier())

      verify(mockDepartureConnector, times(0)).sendMessage(any(), any())(any())
      verify(mockDestinationConnector, times(1)).sendMessage(any(), any())(any())
      verify(mockGuaranteeConnector, times(0)).sendMessage(any(), any())(any())
    }

    "use GuaranteeConnector when forwarding a 917 message with a GUA header" in {
      val messageType              = XMLSubmissionNegativeAcknowledgement
      val mockDepartureConnector   = mock[DepartureConnector]
      val mockDestinationConnector = mock[DestinationConnector]
      val mockGuaranteeConnector   = mock[GuaranteeConnector]
      val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-GUA-1-1").get

      when(mockGuaranteeConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
      val sut = new RoutingService(mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector)

      sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)(HeaderCarrier())

      verify(mockDepartureConnector, times(0)).sendMessage(any(), any())(any())
      verify(mockDestinationConnector, times(0)).sendMessage(any(), any())(any())
      verify(mockGuaranteeConnector, times(1)).sendMessage(any(), any())(any())
    }
  }
}
