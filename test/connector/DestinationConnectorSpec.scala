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

package connector

import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import helper.WireMockServerHandler
import org.scalacheck.Gen
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.{FreeSpec, MustMatchers}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.Application
import play.api.http.Status._
import play.api.inject.guice.GuiceApplicationBuilder
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global

class DestinationConnectorSpec extends FreeSpec
  with MustMatchers
  with GuiceOneAppPerSuite
  with ScalaFutures
  with IntegrationPatience
  with WireMockServerHandler
  with ScalaCheckPropertyChecks
  with MockitoSugar {

  private val startUrl = "transit-movements-trader-at-destination"
  private val sampleXml = "<xml>test</xml>"
  implicit val hc: HeaderCarrier = HeaderCarrier()

  override lazy val app: Application = new GuiceApplicationBuilder()
    .configure(
      conf = "microservice.services.trader-at-destination.port" -> server.port()
    )
    .build()

  lazy val connector: DestinationConnector = app.injector.instanceOf[DestinationConnector]

  private val messageSender = "MDTP-1-1"

  "DestinationConnector" - {
    "must return status as OK for valid input request" in {

      server.stubFor(
        post(urlEqualTo(s"/$startUrl/movements/arrivals/$messageSender/goods-released"))
          .willReturn(
            aResponse()
              .withStatus(OK)
          )
      )

      val result = connector.sendMessage(sampleXml, messageSender)
      result.futureValue.status mustBe OK
    }

    "must return an exception when an error response is returned from sendMessage" in {

      val errorResponses: Gen[Int] = Gen.chooseNum(400, 599)

      forAll(errorResponses) {
        errorResponse =>
          server.stubFor(
            post(urlEqualTo(s"/$startUrl/movements/arrivals/$messageSender/goods-released"))
              .willReturn(
                aResponse()
                  .withStatus(errorResponse)
              )
          )

          val result = connector.sendMessage(sampleXml, messageSender)
          whenReady(result.failed) {
            _ mustBe an[Exception]
          }
      }
    }
  }
}


