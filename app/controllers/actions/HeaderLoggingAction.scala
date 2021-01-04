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
import play.api.http.HeaderNames
import play.api.mvc._
import utils.HttpHeaders

import scala.concurrent.{ExecutionContext, Future}

class HeaderLoggingAction @Inject()(val executionContext: ExecutionContext)
  extends ActionRefiner[Request, Request]
    with Logging {

  override protected def refine[A](
    request: Request[A]
  ): Future[Either[Result, Request[A]]] = {
    logger.info(
      s"""
         |Headers in inbound request:\n
         |${HttpHeaders.X_CORRELATION_ID}: ${request.headers.get(HttpHeaders.X_CORRELATION_ID).getOrElse("undefined")}\n
         |${HttpHeaders.X_REQUEST_ID}: ${request.headers.get(HttpHeaders.X_REQUEST_ID).getOrElse("undefined")}\n
         |${HttpHeaders.X_MESSAGE_TYPE}: ${request.headers.get(HttpHeaders.X_MESSAGE_TYPE).getOrElse("undefined")}\n
         |${HttpHeaders.X_MESSAGE_RECIPIENT}: ${request.headers.get(HttpHeaders.X_MESSAGE_RECIPIENT).getOrElse("undefined")}\n
         |${HeaderNames.CONTENT_TYPE}: ${request.headers.get(HeaderNames.CONTENT_TYPE).getOrElse("undefined")}
         |""".stripMargin)
    Future.successful(Right(request))
  }
}
