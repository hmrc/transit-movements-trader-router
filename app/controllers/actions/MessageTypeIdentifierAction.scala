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

package controllers.actions

import com.google.inject.Inject
import logging.Logging
import models.MessageType
import models.requests.MessageRecipientRequest
import models.requests.RoutableRequest
import play.api.mvc.ActionRefiner
import play.api.mvc.Result
import play.api.mvc.Results.BadRequest
import play.api.mvc.Results.NotImplemented

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class MessageTypeIdentifierActionProvider @Inject() (implicit ec: ExecutionContext) {

  def apply(): ActionRefiner[MessageRecipientRequest, RoutableRequest] =
    new MessageTypeIdentifierAction(ec)
}

class MessageTypeIdentifierAction(val ec: ExecutionContext) extends ActionRefiner[MessageRecipientRequest, RoutableRequest] with Logging {

  override protected def refine[A](
    request: MessageRecipientRequest[A]
  ): Future[Either[Result, RoutableRequest[A]]] =
    Future.successful {
      for {
        headerValue <- request.headers
          .get("X-Message-Type")
          .toRight {
            val message = "Missing X-Message-Type header value"
            logger.error(message)
            BadRequest(message)
          }

        messageType <- MessageType
          .fromHeaderValue(headerValue)
          .toRight {
            val message = s"Invalid X-Message-Type header value: $headerValue"
            logger.error(message)
            NotImplemented(message)
          }
      } yield RoutableRequest(request, messageType)
    }

  override protected def executionContext: ExecutionContext = ec
}
