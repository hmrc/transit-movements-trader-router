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

package utils

import base.SpecBase
import org.scalacheck.Gen
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.http.Status
import play.api.mvc.Result
import uk.gov.hmrc.http.HttpResponse
import play.api.test.Helpers._

import scala.concurrent.Future

class ResponseHelperSpec extends SpecBase with BeforeAndAfterEach with ScalaCheckPropertyChecks {

  class Harness extends ResponseHelper {}

  val clientErrorGenerator: Gen[Int] = Gen.oneOf(Seq(Status.UNAUTHORIZED, Status.FORBIDDEN, Status.NOT_FOUND))
  val serverErrorGenerator: Gen[Int] = Gen.oneOf(Seq(Status.INTERNAL_SERVER_ERROR, Status.NOT_IMPLEMENTED, Status.SERVICE_UNAVAILABLE, Status.GATEWAY_TIMEOUT))

  "handleNon2xx" - {

    val standardMessage = "Incoming Router Rejected: Downstream service rejected"

    "if status is 400 and there is a body, returns a BadRequest with a message including the body" in {
      val result: Result = new Harness().handleNon2xx(HttpResponse(400, "test text"))
      result.body.isKnownEmpty mustBe false
      contentAsString(Future.successful(result)).endsWith("test text") mustBe true
      println(contentAsString(Future.successful(result)))
      contentAsString(Future.successful(result)).startsWith(standardMessage) mustBe true
      status(Future.successful(result)) mustBe 400
    }

    "if status if 400 and there is no body, returns a 400 with a message" in {
      val result: Result = new Harness().handleNon2xx(HttpResponse(400))
      result.body.isKnownEmpty mustBe false
      status(Future.successful(result)) mustBe 400
      contentAsString(Future.successful(result)).startsWith(standardMessage) mustBe true
    }

    "if status is 4xx (but not 400) and there is a body, returns a status with the body" in {

      forAll(clientErrorGenerator) {
        code =>
          val result = new Harness().handleNon2xx(HttpResponse(code, "testBody"))

          result.body.isKnownEmpty mustBe false
      }
    }

    "if status is 4xx (but not 400) and there is no body, returns a status without a body" in {
      forAll(clientErrorGenerator) {
        code =>
          val result = new Harness().handleNon2xx(HttpResponse(code, null))

          result.body.isKnownEmpty mustBe true
      }
    }

    "if status is not a 4xx (but not 400), return a simple status code" in {
      forAll(serverErrorGenerator) {
        code =>
          val result = new Harness().handleNon2xx(HttpResponse(code, null))

          result.body.isKnownEmpty mustBe true
      }
    }
  }

}
