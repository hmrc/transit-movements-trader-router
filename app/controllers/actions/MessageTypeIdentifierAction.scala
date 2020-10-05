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

package controllers.actions

import com.google.inject.{ImplementedBy, Inject}
import models.MessageType
import models.requests.{MessageRecipientRequest, RoutableRequest}
import play.api.Logger
import play.api.mvc.{ActionBuilder, ActionRefiner, ActionTransformer, AnyContent, DefaultActionBuilder, Request, Result}
import play.api.mvc.Results.BadRequest

import scala.concurrent.{ExecutionContext, Future}

class MessageTypeIdentifierActionProvider @Inject()(
     buildDefault: DefaultActionBuilder
   )(implicit ec: ExecutionContext){
  def apply(): ActionRefiner[MessageRecipientRequest, RoutableRequest] =
    new MessageTypeIdentifierAction(ec)
}

class MessageTypeIdentifierAction(val ec: ExecutionContext)
  extends ActionRefiner[MessageRecipientRequest, RoutableRequest] {

  private lazy val logger = Logger(getClass)

  override protected def refine[A](
                                    request: MessageRecipientRequest[A]
                                  ): Future[Either[Result, RoutableRequest[A]]] = {
    println("start")
      request.headers
        .get("X-Message-Type") match {
        case None => {
          logger.debug("BadRequest: missing header key X-Message-Type")
          Future.successful(Left(BadRequest("BadRequest: missing header key X-Message-Type")))
        }
        case Some(messageType) =>
          MessageType.values.filter(x => x.code == messageType).headOption match {
            case None => {
              logger.debug("BadRequest: X-Message-Type header value is unsupported or invalid")
              Future.successful(Left(BadRequest("BadRequest: X-Message-Type header value is unsupported or invalid")))
            }
            case Some(mt) => Future.successful(Right(RoutableRequest(request, mt)))
          }

      }
  }

  override protected def executionContext: ExecutionContext = ec
}