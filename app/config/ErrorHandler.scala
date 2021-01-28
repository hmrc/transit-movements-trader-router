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

import javax.inject.Inject
import logging.Logging
import play.api.Configuration
import play.api.mvc.{RequestHeader, Result}
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.play.bootstrap.backend.http.JsonErrorHandler
import uk.gov.hmrc.play.bootstrap.config.HttpAuditEvent

import scala.concurrent.{ExecutionContext, Future}

class ErrorHandler @Inject()(auditConnector: AuditConnector,
                             httpAuditEvent: HttpAuditEvent,
                             configuration: Configuration
                            )(implicit ec: ExecutionContext)
  extends JsonErrorHandler(auditConnector, httpAuditEvent, configuration) with Logging {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    logger.warn(s"Client error for (${request.method}) [${request.uri}] with status: $statusCode and message: $message")
    super.onClientError(request, statusCode, message)
  }

  override def onServerError(request: RequestHeader, ex: Throwable): Future[Result] = {
    logger.warn(s"[onServerError], error for (${request.method}) [${request.uri}] with error: ${ex.getMessage}")
    super.onServerError(request, ex)
  }
}
