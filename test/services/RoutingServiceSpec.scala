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
import config.AppConfig
import connectors.{DepartureConnector, DestinationConnector, GuaranteeConnector, NCTSMonitoringConnector}
import models.{MessageRecipient, MessageType}
import models.MessageType.{XMLSubmissionNegativeAcknowledgement, nctsMonitoringDepartureValues}
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalacheck.Gen
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.Future

class RoutingServiceSpec extends SpecBase with BeforeAndAfterEach with ScalaCheckPropertyChecks {

  val destinationMessageTypes: Gen[MessageType] = Gen.oneOf(MessageType.arrivalValues)
  val departureMessageTypes: Gen[MessageType]   = Gen.oneOf(MessageType.departureValues)
  val guaranteeMessageTypes: Gen[MessageType]   = Gen.oneOf(MessageType.guaranteeValues)
  val nctsMonitoringDepartureMessageTypes: Gen[MessageType]   = Gen.oneOf(MessageType.nctsMonitoringDepartureValues)
  val nonNCTSMonitoringDepartureMessageTypes: Gen[MessageType]   = Gen.oneOf(MessageType.validMessages.diff(nctsMonitoringDepartureValues))

  "sendMessage must" - {
    "use DepartureConnector when forwarding a Departure Message" in {

      forAll(departureMessageTypes) {
        messageType =>
          val mockDepartureConnector   = mock[DepartureConnector]
          val mockDestinationConnector = mock[DestinationConnector]
          val mockGuaranteeConnector   = mock[GuaranteeConnector]
          val mockNCTSMonitoringConnector  = mock[NCTSMonitoringConnector]
          val mockAppConfig            = mock[AppConfig]
          val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-DEP-1-1").get

          when(mockDepartureConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
          when(mockNCTSMonitoringConnector.sendMessage(any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
          when(mockAppConfig.nctsMonitoringEnabled).thenReturn(false)

          val sut = new RoutingService(
            mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector, mockNCTSMonitoringConnector, mockAppConfig
          )

          sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)

          verify(mockDepartureConnector, times(1)).sendMessage(any(), any())(any())
          verify(mockDestinationConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockGuaranteeConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockNCTSMonitoringConnector, times(0)).sendMessage(any())(any())
      }
    }

    "use NCTSMonitoringConnector when forwarding a Departure Message if the message type is relevant" in {

      forAll(nctsMonitoringDepartureMessageTypes) {
        messageType =>
          val mockDepartureConnector   = mock[DepartureConnector]
          val mockDestinationConnector = mock[DestinationConnector]
          val mockGuaranteeConnector   = mock[GuaranteeConnector]
          val mockNCTSMonitoringConnector  = mock[NCTSMonitoringConnector]
          val mockAppConfig            = mock[AppConfig]
          val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-DEP-1-1").get

          when(mockDepartureConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
          when(mockNCTSMonitoringConnector.sendMessage(any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
          when(mockAppConfig.nctsMonitoringEnabled).thenReturn(true)

          val sut = new RoutingService(
            mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector, mockNCTSMonitoringConnector, mockAppConfig
          )

          sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)

          verify(mockNCTSMonitoringConnector, times(1)).sendMessage(any())(any())
      }
    }

    "not use NCTSMonitoringConnector when forwarding a Departure Message if the message type is irrelevant" in {

      forAll(nonNCTSMonitoringDepartureMessageTypes) {
        messageType =>
          val mockDepartureConnector   = mock[DepartureConnector]
          val mockDestinationConnector = mock[DestinationConnector]
          val mockGuaranteeConnector   = mock[GuaranteeConnector]
          val mockNCTSMonitoringConnector  = mock[NCTSMonitoringConnector]
          val mockAppConfig            = mock[AppConfig]
          val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-DEP-1-1").get

          when(mockDepartureConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
          when(mockAppConfig.nctsMonitoringEnabled).thenReturn(true)

          val sut = new RoutingService(
            mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector, mockNCTSMonitoringConnector, mockAppConfig
          )

          sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)

          verify(mockNCTSMonitoringConnector, times(0)).sendMessage(any())(any())
      }
    }

    "not use NCTSMonitoringConnector when the NCTS monitoring feature is not disabled" in {

      forAll(departureMessageTypes) {
        messageType =>
          val mockDepartureConnector   = mock[DepartureConnector]
          val mockDestinationConnector = mock[DestinationConnector]
          val mockGuaranteeConnector   = mock[GuaranteeConnector]
          val mockNCTSMonitoringConnector  = mock[NCTSMonitoringConnector]
          val mockAppConfig            = mock[AppConfig]
          val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-DEP-1-1").get

          when(mockDepartureConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
          when(mockNCTSMonitoringConnector.sendMessage(any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
          when(mockAppConfig.nctsMonitoringEnabled).thenReturn(false)

          val sut = new RoutingService(
            mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector, mockNCTSMonitoringConnector, mockAppConfig
          )

          sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)

          verify(mockDepartureConnector, times(1)).sendMessage(any(), any())(any())
          verify(mockDestinationConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockGuaranteeConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockNCTSMonitoringConnector, times(0)).sendMessage(any())(any())
      }
    }

    "use DestinationConnector when forwarding a Destination Message" in {
      forAll(destinationMessageTypes) {
        messageType =>
          val mockDepartureConnector   = mock[DepartureConnector]
          val mockDestinationConnector = mock[DestinationConnector]
          val mockGuaranteeConnector   = mock[GuaranteeConnector]
          val mockNCTSMonitoringConnector  = mock[NCTSMonitoringConnector]
          val mockAppConfig            = mock[AppConfig]
          val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-ARR-1-1").get

          when(mockDestinationConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))
          when(mockAppConfig.nctsMonitoringEnabled).thenReturn(true)

          val sut = new RoutingService(
            mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector, mockNCTSMonitoringConnector, mockAppConfig
          )

          sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)(HeaderCarrier())

          verify(mockDepartureConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockDestinationConnector, times(1)).sendMessage(any(), any())(any())
          verify(mockGuaranteeConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockNCTSMonitoringConnector, times(0)).sendMessage(any())(any())
      }
    }

    "use GuaranteeConnector when forwarding a Guarantee Message" in {
      forAll(guaranteeMessageTypes) {
        messageType =>
          val mockDepartureConnector   = mock[DepartureConnector]
          val mockDestinationConnector = mock[DestinationConnector]
          val mockGuaranteeConnector   = mock[GuaranteeConnector]
          val mockNCTSMonitoringConnector  = mock[NCTSMonitoringConnector]
          val mockAppConfig            = mock[AppConfig]
          val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-GUA-1-1").get

          when(mockDestinationConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))

          val sut = new RoutingService(
            mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector, mockNCTSMonitoringConnector, mockAppConfig
          )

          sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)(HeaderCarrier())

          verify(mockDepartureConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockDestinationConnector, times(0)).sendMessage(any(), any())(any())
          verify(mockGuaranteeConnector, times(1)).sendMessage(any(), any())(any())
          verify(mockNCTSMonitoringConnector, times(0)).sendMessage(any())(any())
      }
    }

    "use DepartureConnector when forwarding a 917 message with DEP header" in {
      val messageType              = XMLSubmissionNegativeAcknowledgement
      val mockDepartureConnector   = mock[DepartureConnector]
      val mockDestinationConnector = mock[DestinationConnector]
      val mockGuaranteeConnector   = mock[GuaranteeConnector]
      val mockNCTSMonitoringConnector  = mock[NCTSMonitoringConnector]
      val mockAppConfig            = mock[AppConfig]
      val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-DEP-1-1").get

      when(mockDestinationConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))

      val sut = new RoutingService(
        mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector, mockNCTSMonitoringConnector, mockAppConfig
      )

      sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)(HeaderCarrier())

      verify(mockDepartureConnector, times(1)).sendMessage(any(), any())(any())
      verify(mockDestinationConnector, times(0)).sendMessage(any(), any())(any())
      verify(mockGuaranteeConnector, times(0)).sendMessage(any(), any())(any())
      verify(mockNCTSMonitoringConnector, times(0)).sendMessage(any())(any())
    }

    "use DestinationConnector when forwarding a 917 message with an ARR header" in {
      val messageType              = XMLSubmissionNegativeAcknowledgement
      val mockDepartureConnector   = mock[DepartureConnector]
      val mockDestinationConnector = mock[DestinationConnector]
      val mockGuaranteeConnector   = mock[GuaranteeConnector]
      val mockNCTSMonitoringConnector  = mock[NCTSMonitoringConnector]
      val mockAppConfig            = mock[AppConfig]
      val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-ARR-1-1").get

      when(mockDestinationConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))

      val sut = new RoutingService(
        mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector, mockNCTSMonitoringConnector, mockAppConfig
      )

      sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)(HeaderCarrier())

      verify(mockDepartureConnector, times(0)).sendMessage(any(), any())(any())
      verify(mockDestinationConnector, times(1)).sendMessage(any(), any())(any())
      verify(mockGuaranteeConnector, times(0)).sendMessage(any(), any())(any())
      verify(mockNCTSMonitoringConnector, times(0)).sendMessage(any())(any())
    }

    "use GuaranteeConnector when forwarding a 917 message with a GUA header" in {
      val messageType              = XMLSubmissionNegativeAcknowledgement
      val mockDepartureConnector   = mock[DepartureConnector]
      val mockDestinationConnector = mock[DestinationConnector]
      val mockGuaranteeConnector   = mock[GuaranteeConnector]
      val mockNCTSMonitoringConnector  = mock[NCTSMonitoringConnector]
      val mockAppConfig            = mock[AppConfig]
      val messageRecipient         = MessageRecipient.fromHeaderValue("MDTP-GUA-1-1").get

      when(mockGuaranteeConnector.sendMessage(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "")))

      val sut = new RoutingService(
        mockDestinationConnector, mockDepartureConnector, mockGuaranteeConnector, mockNCTSMonitoringConnector, mockAppConfig
      )

      sut.sendMessage(messageRecipient, messageType, <Abc>123</Abc>)(HeaderCarrier())

      verify(mockDepartureConnector, times(0)).sendMessage(any(), any())(any())
      verify(mockDestinationConnector, times(0)).sendMessage(any(), any())(any())
      verify(mockGuaranteeConnector, times(1)).sendMessage(any(), any())(any())
      verify(mockNCTSMonitoringConnector, times(0)).sendMessage(any())(any())
    }
  }
}
