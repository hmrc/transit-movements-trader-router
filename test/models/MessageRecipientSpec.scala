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

import base.SpecBase
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class MessageRecipientSpec extends SpecBase with ScalaCheckDrivenPropertyChecks {

  "MessageRecipient must be" - {
    "DepartureRecipient" in {
      val messageRecipient = MessageRecipient("MDTP-DEP-1-1")
      messageRecipient shouldBe a[DepartureRecipient]
    }
    "ArrivalRecipient" in {
      val messageRecipient = MessageRecipient("MDTP-ARR-1-1")
      messageRecipient shouldBe an[ArrivalRecipient]
    }
    "ArrivalRecipient as default" in {
      val messageRecipient = MessageRecipient("MDTP-1-1")
      messageRecipient shouldBe an[ArrivalRecipient]
    }
  }
}
