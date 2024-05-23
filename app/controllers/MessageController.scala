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

package controllers

import com.codahale.metrics.MetricRegistry
import controllers.actions.{AnalyseMessageActionProvider, MessageRecipientIdentifierActionProvider, MessageTypeIdentifierActionProvider}
import logging.Logging
import metrics.HasActionMetrics
import metrics.MetricsKeys.Endpoints._
import play.api.mvc.{Action, ControllerComponents}
import services.RoutingService
import uk.gov.hmrc.http.HttpErrorFunctions
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.xml.NodeSeq

class MessageController @Inject() (
  messageRecipientIdentifier: MessageRecipientIdentifierActionProvider,
  messageTypeIdentifier: MessageTypeIdentifierActionProvider,
  cc: ControllerComponents,
  routingService: RoutingService,
  analyseMessage: AnalyseMessageActionProvider,
  val metrics: MetricRegistry
)(implicit val ec: ExecutionContext)
    extends BackendController(cc)
    with HttpErrorFunctions
    with Logging
    with HasActionMetrics {

  def handleMessage(): Action[NodeSeq] =
    withMetricsTimerAction(HandleMessage) {
      (messageRecipientIdentifier() andThen messageTypeIdentifier() andThen analyseMessage()).async(parse.xml) {
        implicit request =>
          logger.debug("routing message")

          routingService
            .sendMessage(request.messageRecipient, request.messageType, request.body)
            .map {
              response =>
                logger.debug(s"routing service response ${response.status} ${response.body}")

                response.status match {
                  case status if is2xx(status) =>
                    response.header(LOCATION) match {
                      case Some(value) =>
                        Status(response.status).withHeaders(LOCATION -> value)
                      case None =>
                        logger.warn("No location header in downstream response")
                        Status(response.status)
                    }
                  case 404 => Ok
                  case 400 if response.body != null =>
                    logger.warn(s"Incoming Router Rejected: Downstream service rejected with following message: ${response.body}")
                    BadRequest(response.body)
                  case 400 =>
                    logger.warn("Incoming Router Rejected: Downstream service rejected with no message")
                    BadRequest
                  case _ =>  logger.warn("Incoming Router Rejected: Unknown error occurred")
                    Status(response.status)
                }
            }
      }
    }
}
