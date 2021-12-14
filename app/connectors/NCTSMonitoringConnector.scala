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

package connectors

import com.kenshoo.play.metrics.Metrics
import config.AppConfig
import logging.Logging
import metrics.HasMetrics
import models.Movement
import play.api.libs.json.Json
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class NCTSMonitoringConnector @Inject()(
  config: AppConfig,
  http: HttpClient,
  val metrics: Metrics
)(implicit ec: ExecutionContext)
    extends HasMetrics with Logging {

  def sendMessage(movement: Movement)(implicit hc: HeaderCarrier): Future[HttpResponse] = {

    val nctsMonitoringUrl = s"${config.nctsMonitoringUrl.baseUrl}"

    http.POSTString[HttpResponse](nctsMonitoringUrl, Json.toJson(movement).toString).map { response =>
      response.status match {
        case 200 => response
        case _ =>
          logger.warn(s"[NCTSMonitoringConnector][sendMessage] Failed to send inbound message with status ${response.status}")
          response
      }
    }
  }
}
