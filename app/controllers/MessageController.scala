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

import com.kenshoo.play.metrics.Metrics
import controllers.actions.{AnalyseMessageActionProvider, MessageRecipientIdentifierActionProvider, MessageTypeIdentifierActionProvider}
import logging.Logging
import javax.inject.Inject
import metrics.HasActionMetrics
import play.api.mvc.{Action, ControllerComponents}
import services.RoutingService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import utils.ResponseHelper

import scala.concurrent.ExecutionContext
import scala.xml.NodeSeq
import metrics.MetricsKeys.Endpoints._

class MessageController @Inject()(
  messageRecipientIdentifier: MessageRecipientIdentifierActionProvider,
  messageTypeIdentifier: MessageTypeIdentifierActionProvider,
  cc: ControllerComponents,
  routingService: RoutingService,
  analyseMessage: AnalyseMessageActionProvider,
  val metrics: Metrics
)(implicit val ec: ExecutionContext)
    extends BackendController(cc) with ResponseHelper with Logging with HasActionMetrics {

  def handleMessage(): Action[NodeSeq] =
    withMetricsTimerAction(HandleMessage) {
      (messageRecipientIdentifier() andThen messageTypeIdentifier() andThen analyseMessage()).async(parse.xml) { implicit request =>
        routingService
          .sendMessage(request.messageRecipient, request.directable, request.body, request.headers)
          .map {
            response =>
              response.status match {
                case status if is2xx(status) =>
                  response.header(LOCATION) match {
                    case Some(value) =>
                      Status(response.status).withHeaders(LOCATION -> value)
                    case None =>
                      logger.warn("No location header in downstream response")
                      Status(response.status)
                  }
                case _ =>
                  handleNon2xx(response)
              }
          }
      }
    }
}
