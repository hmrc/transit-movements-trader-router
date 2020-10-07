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

package services

import connectors.{DepartureConnector, DestinationConnector}
import javax.inject.Inject
import models.{Directable, MessageType}
import play.api.mvc.Headers
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import scala.concurrent.Future
import scala.xml.NodeSeq
import models.ArrivalMessage
import models.DepartureMessage

class RoutingService @Inject()(destinationConnector: DestinationConnector, departureConnector: DepartureConnector) {

    def sendMessage(messageRecipient: String, directable: Directable, messageBody: NodeSeq, headers: Headers)(implicit hc: HeaderCarrier): Future[HttpResponse] = {
            directable match {
                case _:ArrivalMessage =>
                    destinationConnector.sendMessage(messageRecipient, messageBody, headers)
                case _:DepartureMessage =>
                    departureConnector.sendMessage(messageRecipient, messageBody, headers)
            }
        }
}
