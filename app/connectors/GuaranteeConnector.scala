/*
 * Copyright 2022 HM Revenue & Customs
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

import com.codahale.metrics.MetricRegistry
import config.AppConfig
import metrics.HasMetrics
import metrics.MetricsKeys.Connectors._
import models.MessageRecipient
import play.api.http.HeaderNames
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.xml.NodeSeq

class GuaranteeConnector @Inject() (
  config: AppConfig,
  http: HttpClient,
  val metrics: MetricRegistry
)(implicit ec: ExecutionContext)
    extends HasMetrics {

  def sendMessage(
    messageRecipient: MessageRecipient,
    requestData: NodeSeq
  )(implicit hc: HeaderCarrier): Future[HttpResponse] = withMetricsTimerResponse(RouteToGuarantee) {

    val serviceUrl =
      s"${config.guaranteeUrl.baseUrl}/balances/${messageRecipient.headerValue}"

    val headers = hc.headers(
      Seq(
        HeaderNames.CONTENT_TYPE,
        "X-Message-Recipient",
        "X-Message-Type"
      )
    )

    http.POSTString[HttpResponse](serviceUrl, requestData.toString, headers)
  }
}
