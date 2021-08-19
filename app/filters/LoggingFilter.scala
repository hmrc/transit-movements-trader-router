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

package filters

import akka.stream.Materializer
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import javax.inject.Inject
import logging.Logging
import play.api.http.HeaderNames
import play.api.mvc.Filter
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import utils.HttpHeaders

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class LoggingFilter @Inject() (implicit
  val mat: Materializer,
  ec: ExecutionContext
) extends Filter
    with Logging {

  def apply(
    next: RequestHeader => Future[Result]
  )(rh: RequestHeader): Future[Result] =
    next(rh).map {
      result =>
        if (rh.headers.get(HttpHeaders.X_MESSAGE_RECIPIENT).isDefined) {
          val sink =
            Sink.fold[String, ByteString]("") {
              case (acc, str) =>
                acc + str.decodeString("UTF-8")
            }

          result.body.dataStream.runWith(sink).map {
            body =>
              val logMessage =
                s"""
              |${HttpHeaders.X_CORRELATION_ID}: ${rh.headers.get(HttpHeaders.X_CORRELATION_ID).getOrElse("undefined")}
              |${HttpHeaders.X_REQUEST_ID}: ${rh.headers.get(HttpHeaders.X_REQUEST_ID).getOrElse("undefined")}
              |${HttpHeaders.X_MESSAGE_TYPE}: ${rh.headers.get(HttpHeaders.X_MESSAGE_TYPE).getOrElse("undefined")}
              |${HttpHeaders.X_MESSAGE_RECIPIENT}: ${rh.headers.get(HttpHeaders.X_MESSAGE_RECIPIENT).getOrElse("undefined")}
              |${HeaderNames.CONTENT_TYPE}: ${rh.headers.get(HeaderNames.CONTENT_TYPE).getOrElse("undefined")}
              |Response status: ${result.header.status}
              |Response body:  $body
           """.stripMargin

              if (result.header.status < 400) {
                logger.info(logMessage)
              } else if (result.header.status >= 400 && result.header.status < 499) {
                logger.warn(logMessage)
              } else {
                logger.error(logMessage)
              }
          }
        }
        result
    }
}
