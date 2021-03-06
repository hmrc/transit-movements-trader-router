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
import models.MessageRecipient
import javax.inject.Inject
import metrics.HasMetrics
import metrics.MetricsKeys.Connectors.RouteToArrivals
import play.api.mvc.Headers
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.NodeSeq

class DestinationConnector @Inject()(
  val config: AppConfig,
  val http: HttpClient,
  val metrics: Metrics
)(implicit ec: ExecutionContext) extends HasMetrics {

  def sendMessage(
                   xMessageRecipient: MessageRecipient,
                   requestData: NodeSeq,
                   headers: Headers
  )(implicit hc: HeaderCarrier): Future[HttpResponse] = withMetricsTimerResponse(RouteToArrivals){

    val serviceUrl =
      s"${config.traderAtDestinationUrl.baseUrl}/movements/arrivals/${xMessageRecipient.headerValue}/messages/eis"

    val header = headers.headers.filter(
      header =>
        header._1.equalsIgnoreCase ("X-Message-Recipient") || header._1.equalsIgnoreCase("X-Message-Type") || header._1.equalsIgnoreCase("Content-Type")
    )

    // TODO: Determine which headers need to be sent on
    http.POSTString[HttpResponse](serviceUrl, requestData.toString, header)
  }
}
