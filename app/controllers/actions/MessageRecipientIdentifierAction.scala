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
import models.MessageRecipient
import models.requests
import models.requests.MessageRecipientRequest
import play.api.mvc.Results.BadRequest
import play.api.mvc._

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class MessageRecipientIdentifierActionProvider @Inject() (
  buildDefault: DefaultActionBuilder
)(implicit val ec: ExecutionContext) {

  def apply(): ActionBuilder[MessageRecipientRequest, AnyContent] =
    buildDefault andThen
      new MessageRecipientIdentifierAction(ec)
}

class MessageRecipientIdentifierAction(val executionContext: ExecutionContext) extends ActionRefiner[Request, MessageRecipientRequest] with Logging {

  override protected def refine[A](
    request: Request[A]
  ): Future[Either[Result, MessageRecipientRequest[A]]] =
    Future.successful {
      for {
        headerValue <- request.headers
          .get("X-Message-Recipient")
          .toRight {
            val message = "Missing X-Message-Recipient header value"
            logger.error(message)
            BadRequest(message)
          }

        messageRecipient <- MessageRecipient
          .fromHeaderValue(headerValue)
          .toRight {
            val message = "Invalid X-Message-Recipient header value"
            logger.error(message)
            BadRequest(message)
          }

      } yield requests.MessageRecipientRequest(request, messageRecipient)
    }
}
