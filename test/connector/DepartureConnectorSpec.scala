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

package connector

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, urlEqualTo}
import connectors.DepartureConnector
import helper.WireMockServerHandler
import models.MessageRecipient
import org.scalacheck.Gen
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import play.api.http.Status.OK
import play.api.mvc.Headers
import uk.gov.hmrc.http.HeaderCarrier
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import scala.xml.Elem

class DepartureConnectorSpec extends AnyFreeSpec
  with Matchers
  with ScalaFutures
  with IntegrationPatience
  with WireMockServerHandler
  with ScalaCheckPropertyChecks
  with MockitoSugar
   {

  private val startUrl =
    "transits-movements-trader-at-departure/movements/departures"
  val sampleXml: Elem = <xml>test</xml>
  val messageRecipient = MessageRecipient("MDTP-DEP-1-1")

  implicit val hc: HeaderCarrier =
    HeaderCarrier().withExtraHeaders("X-Test-Header" -> "X-Test-Header-Value")

  lazy val connector: DepartureConnector =
    app.injector.instanceOf[DepartureConnector]

  "DepartureConnector" - {
    "must return status as OK for valid input request" in {

      server.stubFor(
        post(urlEqualTo(s"/$startUrl/${messageRecipient.headerValue}/messages/eis"))
          .willReturn(
            aResponse()
              .withStatus(OK)
          )
      )

      val result = connector.sendMessage(messageRecipient, sampleXml, Headers())
      result.futureValue.status mustBe OK
    }

    "must return a HttpResponse with that status code" in {

      val errorResponses: Gen[Int] = Gen.chooseNum(400, 599)

      forAll(errorResponses) { errorResponse =>
        server.stubFor(
          post(urlEqualTo(s"/$startUrl/${messageRecipient.headerValue}/messages/eis"))
            .willReturn(
              aResponse()
                .withStatus(errorResponse)
            )
        )

        val result = connector.sendMessage(messageRecipient, sampleXml, Headers())

        result.futureValue.status mustBe errorResponse

      }
    }
  }

  override protected def portConfigKey: String = "microservice.services.trader-at-departure.port"
}
