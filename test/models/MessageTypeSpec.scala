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
import models.MessageType._
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import play.api.libs.json.{JsError, JsNumber, JsString, JsSuccess, Json}

class MessageTypeSpec extends SpecBase with ScalaCheckDrivenPropertyChecks {

  "MessageType must contain" - {
    "PositiveAcknowledgement" in {
      MessageType.allMessages must contain(PositiveAcknowledgement)
      PositiveAcknowledgement.code mustEqual "IE928"
      PositiveAcknowledgement.rootNode mustEqual "CC928A"
      MessageType.departureValues must contain(PositiveAcknowledgement)
    }

    "MrnAllocated" in {
      MessageType.allMessages must contain(MrnAllocated)
      MrnAllocated.code mustEqual "IE028"
      MrnAllocated.rootNode mustEqual "CC028A"
      MessageType.departureValues must contain(MrnAllocated)
    }

    "DeclarationRejected" in {
      MessageType.allMessages must contain(DeclarationRejected)
      DeclarationRejected.code mustEqual "IE016"
      DeclarationRejected.rootNode mustEqual "CC016A"
      MessageType.departureValues must contain(DeclarationRejected)
    }

    "ControlDecisionNotification" in {
      MessageType.allMessages must contain(ControlDecisionNotification)
      ControlDecisionNotification.code mustEqual "IE060"
      ControlDecisionNotification.rootNode mustEqual "CC060A"
      MessageType.departureValues must contain(ControlDecisionNotification)
    }

    "NoReleaseForTransit" in {
      MessageType.allMessages must contain(NoReleaseForTransit)
      NoReleaseForTransit.code mustEqual "IE051"
      NoReleaseForTransit.rootNode mustEqual "CC051B"
      MessageType.departureValues must contain(NoReleaseForTransit)
    }

    "ReleaseForTransit" in {
      MessageType.allMessages must contain(ReleaseForTransit)
      ReleaseForTransit.code mustEqual "IE029"
      ReleaseForTransit.rootNode mustEqual "CC029B"
      MessageType.departureValues must contain(ReleaseForTransit)
    }

    "CancellationDecision" in {
      MessageType.allMessages must contain(CancellationDecision)
      CancellationDecision.code mustEqual "IE009"
      CancellationDecision.rootNode mustEqual "CC009A"
      MessageType.departureValues must contain(CancellationDecision)
    }

    "WriteOffNotification" in {
      MessageType.allMessages must contain(WriteOffNotification)
      WriteOffNotification.code mustEqual "IE045"
      WriteOffNotification.rootNode mustEqual "CC045A"
      MessageType.departureValues must contain(WriteOffNotification)
    }

    "GuaranteeNotValid" in {
      MessageType.allMessages must contain(GuaranteeNotValid)
      GuaranteeNotValid.code mustEqual "IE055"
      GuaranteeNotValid.rootNode mustEqual "CC055A"
      MessageType.departureValues must contain(GuaranteeNotValid)
    }

    "ArrivalRejection" in {
      MessageType.allMessages must contain(ArrivalRejection)
      ArrivalRejection.code mustEqual "IE008"
      ArrivalRejection.rootNode mustEqual "CC008A"
      MessageType.arrivalValues must contain(ArrivalRejection)
    }

    "UnloadingPermission" in {
      MessageType.allMessages must contain(UnloadingPermission)
      UnloadingPermission.code mustEqual "IE043"
      UnloadingPermission.rootNode mustEqual "CC043A"
      MessageType.arrivalValues must contain(UnloadingPermission)
    }

    "UnloadingRemarksRejection" in {
      MessageType.allMessages must contain(UnloadingRemarksRejection)
      UnloadingRemarksRejection.code mustEqual "IE058"
      UnloadingRemarksRejection.rootNode mustEqual "CC058A"
      MessageType.arrivalValues must contain(UnloadingRemarksRejection)
    }

    "GoodsReleased" in {
      MessageType.allMessages must contain(GoodsReleased)
      GoodsReleased.code mustEqual "IE025"
      GoodsReleased.rootNode mustEqual "CC025A"
      MessageType.arrivalValues must contain(GoodsReleased)
    }

    "XMLSubmissionNegativeAcknowledgement" in {
      MessageType.allMessages must contain(XMLSubmissionNegativeAcknowledgement)
      XMLSubmissionNegativeAcknowledgement.code mustEqual "IE917"
      XMLSubmissionNegativeAcknowledgement.rootNode mustEqual "CC917A"
      MessageType.arrivalValues must contain(XMLSubmissionNegativeAcknowledgement)
    }
  }

  "Json reads and writes" - {
    "writes" in {
      forAll(Gen.oneOf[MessageType](MessageType.allMessages)) {
        messageType =>
          Json.toJson(messageType) mustEqual JsString(messageType.code)
      }
    }

    "reads" - {
      "returns the message type when given the code for a message" in {
        forAll(Gen.oneOf[MessageType](MessageType.allMessages)) {
          message =>
            JsString(message.code).validate[MessageType] mustEqual JsSuccess(message)
        }
      }

      "returns an error when the message type code is not recognised" in {
        val invalidMessageCode = JsString("InvalidMessageCode")

        invalidMessageCode.validate[MessageType] mustEqual JsError("Not a recognised value")
      }

      "returns an error when the message type code is not a string" in {
        val invalidMessageCode = JsNumber(1)

        invalidMessageCode.validate[MessageType] mustEqual JsError("Invalid type. Expected a JsString got a class play.api.libs.json.JsNumber")
      }

    }
  }

}
