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

import com.google.inject.Inject
import models.requests
import models.requests.MessageRecipientRequest
import play.api.{Logger, mvc}
import play.api.mvc._
import play.api.mvc.Results.BadRequest

import scala.concurrent.{ExecutionContext, Future}

class MessageRecipientIdentifierActionProvider @Inject()(
  buildDefault: DefaultActionBuilder
)(implicit val executionContext: ExecutionContext) {
  def apply(): ActionBuilder[MessageRecipientRequest, AnyContent] =
    buildDefault andThen (new MessageRecipientIdentifierAction(
      executionContext
    ))
}

class MessageRecipientIdentifierAction(val executionContext: ExecutionContext)
    extends ActionRefiner[Request, MessageRecipientRequest] {

  private lazy val logger = Logger(getClass)

  override protected def refine[A](
    request: Request[A]
  ): Future[Either[Result, MessageRecipientRequest[A]]] =
    Future.successful {
      request.headers
        .get("X-Message-Recipient")
        .toRight {
          logger.error("BadRequest: missing header key X-Message-Recipient")
          BadRequest
        }
        .right
        .map(requests.MessageRecipientRequest(request, _))
    }
}
