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
import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class MessageRecipientSpec extends SpecBase with ScalaCheckDrivenPropertyChecks with OptionValues {

  "MessageRecipient must be" - {
    "DepartureRecipient" in {
      val messageRecipient = MessageRecipient.fromHeaderValue("MDTP-DEP-1-1")
      messageRecipient.value shouldBe a[DepartureRecipient]
    }
    "ArrivalRecipient" in {
      val messageRecipient = MessageRecipient.fromHeaderValue("MDTP-ARR-1-1")
      messageRecipient.value shouldBe an[ArrivalRecipient]
    }
    "GuaranteeRecipient" in {
      val messageRecipient = MessageRecipient.fromHeaderValue("MDTP-GUA-1-1")
      messageRecipient.value shouldBe an[GuaranteeRecipient]
    }
    "None when there is no match" in {
      val messageRecipient = MessageRecipient.fromHeaderValue("MDTP-1-1")
      messageRecipient shouldBe None
    }
  }
}
