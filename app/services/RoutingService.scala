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

import connectors.DepartureConnector
import connectors.DestinationConnector
import connectors.GuaranteeConnector
import models.ArrivalMessage
import models.ArrivalRecipient
import models.DepartureMessage
import models.DepartureRecipient
import models.GuaranteeMessage
import models.GuaranteeRecipient
import models.MessageRecipient
import models.MessageType
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpResponse

import javax.inject.Inject
import scala.concurrent.Future
import scala.xml.NodeSeq

class RoutingService @Inject() (destinationConnector: DestinationConnector, departureConnector: DepartureConnector, guaranteeConnector: GuaranteeConnector) {

  def sendMessage(messageRecipient: MessageRecipient, messageType: MessageType, messageBody: NodeSeq)(implicit hc: HeaderCarrier): Future[HttpResponse] =
    messageType match {
      case _: ArrivalMessage =>
        destinationConnector.sendMessage(messageRecipient, messageBody)
      case _: DepartureMessage =>
        departureConnector.sendMessage(messageRecipient, messageBody)
      case _: GuaranteeMessage =>
        guaranteeConnector.sendMessage(messageRecipient, messageBody)
      case _ =>
        messageRecipient match {
          case departureRecipient: DepartureRecipient =>
            departureConnector.sendMessage(departureRecipient, messageBody)
          case arrivalRecipient: ArrivalRecipient =>
            destinationConnector.sendMessage(arrivalRecipient, messageBody)
          case guaranteeRecipient: GuaranteeRecipient =>
            guaranteeConnector.sendMessage(guaranteeRecipient, messageBody)
        }
    }
}
