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

package config

import logging.Logging
import play.api.Configuration
import play.api.http.HeaderNames
import play.api.libs.json.Json
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.backend.http.JsonErrorHandler
import uk.gov.hmrc.play.bootstrap.config.HttpAuditEvent
import utils.HttpHeaders

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class ErrorHandler @Inject() (auditConnector: AuditConnector, httpAuditEvent: HttpAuditEvent, configuration: Configuration)(implicit ec: ExecutionContext)
    extends JsonErrorHandler(auditConnector, httpAuditEvent, configuration)
    with Logging {

  override def onClientError(rh: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    val details = Map(
      "error type"      -> "Client error",
      "request-method"  -> rh.method,
      "request-uri"     -> rh.uri,
      "response-status" -> (statusCode + ""),
      "error-message"   -> message
    ).++(headers(rh))

    val json = Json.toJson(details)
    logger.warn(Json.prettyPrint(json))
    super.onClientError(rh, statusCode, message)
  }

  override def onServerError(rh: RequestHeader, ex: Throwable): Future[Result] = {
    val details = Map(
      "error type"     -> "Server error",
      "request-method" -> rh.method,
      "request-uri"    -> rh.uri,
      "error-message"  -> ex.getMessage
    ).++(headers(rh))

    val json = Json.toJson(details)
    logger.warn(Json.prettyPrint(json))
    super.onServerError(rh, ex)
  }

  def headers(rh: RequestHeader): Map[String, String] =
    Map(
      HttpHeaders.X_CORRELATION_ID    -> rh.headers.get(HttpHeaders.X_CORRELATION_ID).getOrElse("undefined"),
      HttpHeaders.X_REQUEST_ID        -> rh.headers.get(HttpHeaders.X_REQUEST_ID).getOrElse("undefined"),
      HttpHeaders.X_MESSAGE_TYPE      -> rh.headers.get(HttpHeaders.X_MESSAGE_TYPE).getOrElse("undefined"),
      HttpHeaders.X_MESSAGE_RECIPIENT -> rh.headers.get(HttpHeaders.X_MESSAGE_RECIPIENT).getOrElse("undefined"),
      HeaderNames.CONTENT_TYPE        -> rh.headers.get(HeaderNames.CONTENT_TYPE).getOrElse("undefined")
    )
}
