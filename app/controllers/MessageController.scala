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

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents, Result}
import uk.gov.hmrc.play.bootstrap.controller.BackendController
import config.AppConfig
import connector.DestinationConnector
import play.api.Logger
import uk.gov.hmrc.http.{BadRequestException, NotFoundException, Upstream4xxResponse}
import utils.MessageType

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal
import scala.xml.NodeSeq

@Singleton()
class MessageController @Inject()(appConfig: AppConfig,
                                  cc: ControllerComponents,
                                  connector: DestinationConnector
                                 )(implicit val ec: ExecutionContext)
  extends BackendController(cc) {

  def handleMessageType(): Action[NodeSeq] = Action(parse.xml).async { implicit request =>

    (request.headers.get("X-Message-Type"), request.headers.get("X-Message-Sender")) match {
      case (Some(MessageType.GoodsReleaseNotification), Some(messageSender)) =>
        connector.sendMessage(request.body.toString(), messageSender)
        .map(_ => Ok)
        .recover(withPostErrorRecovery)
      case (Some(messageType), _) =>
        Logger.error(s"$messageType is not acceptable")
        Future.successful(NotAcceptable)
      case _ =>
        Logger.error("BadRequest: either header key 'X-Message-Type' is missing or 'X-Message-Sender' is missing")
        Future.successful(BadRequest)
    }
  }

  private def withPostErrorRecovery: PartialFunction[Throwable, Result] = {
    case bre: BadRequestException =>
      Logger.error(s"BadRequest returned from POST: $bre", bre)
      BadRequest
    case nfe: NotFoundException =>
      Logger.error(s"NotFound returned from POST: $nfe", nfe)
      NotFound
    case e4xx: Upstream4xxResponse if e4xx.upstreamResponseCode == FORBIDDEN =>
      Logger.error(s"Forbidden returned from POST: $e4xx", e4xx)
      Forbidden
    case NonFatal(e) =>
      Logger.error(s"Could not POST: $e", e)
      InternalServerError
  }
}
