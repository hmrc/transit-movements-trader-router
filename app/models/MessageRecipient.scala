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

package models

sealed trait MessageRecipient {
  val headerValue: String
}

case class ArrivalRecipient(headerValue: String) extends MessageRecipient

case class DepartureRecipient(headerValue: String) extends MessageRecipient

case class GuaranteeRecipient(headerValue: String) extends MessageRecipient

object MessageRecipient {

  def fromHeaderValue(headerValue: String): Option[MessageRecipient] = {
    val headerPrefix = headerValue.take(9)

    if (headerPrefix.equalsIgnoreCase("MDTP-DEP-"))
      Some(DepartureRecipient(headerValue))
    else if (headerPrefix.equalsIgnoreCase("MDTP-ARR-"))
      Some(ArrivalRecipient(headerValue))
    else if (headerPrefix.equalsIgnoreCase("MDTP-GUA-"))
      Some(GuaranteeRecipient(headerValue))
    else
      None
  }
}
