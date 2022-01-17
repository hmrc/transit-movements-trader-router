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

package connector

import com.github.tomakehurst.wiremock.client.WireMock._
import connectors.NCTSMonitoringConnector
import helper.WireMockServerHandler
import models.{MessageRecipient, Movement}
import org.scalacheck.Gen
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.http.Status.OK
import play.api.http.{HeaderNames, MimeTypes}
import uk.gov.hmrc.http.HeaderCarrier

import java.time.{LocalDateTime, ZoneOffset}

class NCTSMonitoringConnectorSpec
    extends AnyFreeSpec
    with Matchers
    with ScalaFutures
    with IntegrationPatience
    with WireMockServerHandler
    with ScalaCheckPropertyChecks
    with MockitoSugar {

  private val startUrl = "ncts/response-notification"
  val xMessageRecipient = "MDTP-DEP-1-1"
  val messageRecipient: MessageRecipient = MessageRecipient.fromHeaderValue(xMessageRecipient).get

  implicit val hc: HeaderCarrier =
    HeaderCarrier(otherHeaders =
      Seq(
        HeaderNames.CONTENT_TYPE -> MimeTypes.XML,
        "X-Message-Recipient"    -> xMessageRecipient,
        "X-Message-Type"         -> "IE028"
      )
    ).withExtraHeaders("X-Test-Header" -> "X-Test-Header-Value")

  lazy val connector: NCTSMonitoringConnector = app.injector.instanceOf[NCTSMonitoringConnector]

  val testMovement: Movement = Movement(
    "TEST-ID", "IE028", LocalDateTime.ofEpochSecond(1638349126L, 0, ZoneOffset.UTC)
  )

  "NCTSMonitoringConnector" - {
    "must return status as OK for valid input request" in {

      server.stubFor(
        post(urlEqualTo(s"/$startUrl"))
          .willReturn(
            aResponse()
              .withStatus(OK)
          )
      )

      val result = connector.sendMessage(testMovement)

      result.futureValue.status mustBe OK
    }

    "must return a HttpResponse with a failure status code" in {

      val errorResponses: Gen[Int] = Gen.chooseNum(400, 599)

      forAll(errorResponses) {
        errorResponse =>
          server.stubFor(
            post(urlEqualTo(s"/$startUrl"))
              .willReturn(
                aResponse()
                  .withStatus(errorResponse)
              )
          )

          val result = connector.sendMessage(testMovement)

          result.futureValue.status mustBe errorResponse

      }
    }
  }

  override protected def portConfigKey: String = "microservice.services.ncts-monitoring.port"
}
