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

import config.AppConfig
import connectors.{DepartureConnector, DestinationConnector, GuaranteeConnector, NCTSMonitoringConnector}
import models.{ArrivalMessage, ArrivalRecipient, DepartureMessage, DepartureRecipient, GuaranteeMessage, GuaranteeRecipient, MessageRecipient, MessageType, Movement}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpResponse

import java.time.LocalDateTime
import javax.inject.Inject
import scala.concurrent.Future
import scala.xml.NodeSeq

class RoutingService @Inject() (destinationConnector: DestinationConnector, departureConnector: DepartureConnector,
                                guaranteeConnector: GuaranteeConnector, nctsMonitoringConnector: NCTSMonitoringConnector,
                                appConfig: AppConfig) {

  def sendMessage(messageRecipient: MessageRecipient, messageType: MessageType, messageBody: NodeSeq)(implicit hc: HeaderCarrier): Future[HttpResponse] =
    messageType match {
      case _: ArrivalMessage =>
        destinationConnector.sendMessage(messageRecipient, messageBody)
      case _: DepartureMessage =>
        val response = departureConnector.sendMessage(messageRecipient, messageBody)
        if(MessageType.nctsMonitoringDepartureValues.contains(messageType) && appConfig.nctsMonitoringEnabled)
          nctsMonitoringConnector.sendMessage(Movement(messageRecipient.headerValue, messageType.code, LocalDateTime.now))
        response
      case _: GuaranteeMessage =>
        guaranteeConnector.sendMessage(messageRecipient, messageBody)
      case _ =>
        messageRecipient match {
          case departureRecipient: DepartureRecipient =>
            val response = departureConnector.sendMessage(departureRecipient, messageBody)
            if(MessageType.nctsMonitoringDepartureValues.contains(messageType) && appConfig.nctsMonitoringEnabled)
              nctsMonitoringConnector.sendMessage(Movement(departureRecipient.headerValue, messageType.code, LocalDateTime.now))
            response
          case arrivalRecipient: ArrivalRecipient =>
            destinationConnector.sendMessage(arrivalRecipient, messageBody)
          case guaranteeRecipient: GuaranteeRecipient =>
            guaranteeConnector.sendMessage(guaranteeRecipient, messageBody)
        }
    }
}
