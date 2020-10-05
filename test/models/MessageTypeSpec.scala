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

package models

import base.SpecBase
import models.MessageType._
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks
import play.api.libs.json.{JsError, JsNumber, JsString, JsSuccess, Json}

class MessageTypeSpec extends SpecBase with ScalaCheckDrivenPropertyChecks {

  "MessageType must contain" - {
    "PositiveAcknowledgement" in {
      MessageType.values must contain(PositiveAcknowledgement)
      PositiveAcknowledgement.code mustEqual "IE928"
      PositiveAcknowledgement.rootNode mustEqual "CC928A"
      MessageType.departureValues must contain(PositiveAcknowledgement)
    }

    "DepartureDeclaration" in {
      MessageType.values must contain(DepartureDeclaration)
      DepartureDeclaration.code mustEqual "IE015"
      DepartureDeclaration.rootNode mustEqual "CC015B"
      MessageType.departureValues must contain(DepartureDeclaration)
    }

    "MrnAllocated" in {
      MessageType.values must contain(MrnAllocated)
      MrnAllocated.code mustEqual "IE028"
      MrnAllocated.rootNode mustEqual "CC028A"
      MessageType.departureValues must contain(MrnAllocated)
    }

    "DeclarationRejected" in {
      MessageType.values must contain(DeclarationRejected)
      DeclarationRejected.code mustEqual "IE016"
      DeclarationRejected.rootNode mustEqual "CC016A"
      MessageType.departureValues must contain(DeclarationRejected)
    }

    "ControlDecisionNotification" in {
      MessageType.values must contain(ControlDecisionNotification)
      ControlDecisionNotification.code mustEqual "IE060"
      ControlDecisionNotification.rootNode mustEqual "CC060A"
      MessageType.departureValues must contain(ControlDecisionNotification)
    }

    "NoReleaseForTransit" in {
      MessageType.values must contain(NoReleaseForTransit)
      NoReleaseForTransit.code mustEqual "IE051"
      NoReleaseForTransit.rootNode mustEqual "CC051A"
      MessageType.departureValues must contain(NoReleaseForTransit)
    }

    "ReleaseForTransit" in {
      MessageType.values must contain(ReleaseForTransit)
      ReleaseForTransit.code mustEqual "IE029"
      ReleaseForTransit.rootNode mustEqual "CC029A"
      MessageType.departureValues must contain(ReleaseForTransit)
    }

    "DeclarationCancellationRequest" in {
      MessageType.values must contain(DeclarationCancellationRequest)
      DeclarationCancellationRequest.code mustEqual "IE014"
      DeclarationCancellationRequest.rootNode mustEqual "CC014A"
      MessageType.departureValues must contain(DeclarationCancellationRequest)
    }

    "CancellationDecision" in {
      MessageType.values must contain(CancellationDecision)
      CancellationDecision.code mustEqual "IE009"
      CancellationDecision.rootNode mustEqual "CC009A"
      MessageType.departureValues must contain(CancellationDecision)
    }

    "WriteOffNotification" in {
      MessageType.values must contain(WriteOffNotification)
      WriteOffNotification.code mustEqual "IE045"
      WriteOffNotification.rootNode mustEqual "CC045A"
      MessageType.departureValues must contain(WriteOffNotification)
    }

    "GuaranteeNotValid" in {
      MessageType.values must contain(GuaranteeNotValid)
      GuaranteeNotValid.code mustEqual "IE055"
      GuaranteeNotValid.rootNode mustEqual "CC055A"
      MessageType.departureValues must contain(GuaranteeNotValid)
    }

    "ArrivalNotification" in {
      MessageType.values must contain(ArrivalNotification)
      ArrivalNotification.code mustEqual "IE007"
      ArrivalNotification.rootNode mustEqual "CC007A"
      MessageType.arrivalValues must contain(ArrivalNotification)
    }

    "ArrivalRejection" in {
      MessageType.values must contain(ArrivalRejection)
      ArrivalRejection.code mustEqual "IE008"
      ArrivalRejection.rootNode mustEqual "CC008A"
      MessageType.arrivalValues must contain(ArrivalRejection)
    }

    "UnloadingPermission" in {
      MessageType.values must contain(UnloadingPermission)
      UnloadingPermission.code mustEqual "IE043"
      UnloadingPermission.rootNode mustEqual "CC043A"
      MessageType.arrivalValues must contain(UnloadingPermission)
    }

    "UnloadingRemarks" in {
      MessageType.values must contain(UnloadingRemarks)
      UnloadingRemarks.code mustEqual "IE044"
      UnloadingRemarks.rootNode mustEqual "CC044A"
      MessageType.arrivalValues must contain(UnloadingRemarks)
    }

    "UnloadingRemarksRejection" in {
      MessageType.values must contain(UnloadingRemarksRejection)
      UnloadingRemarksRejection.code mustEqual "IE058"
      UnloadingRemarksRejection.rootNode mustEqual "CC058A"
      MessageType.arrivalValues must contain(UnloadingRemarksRejection)
    }

    "GoodsReleased" in {
      MessageType.values must contain(GoodsReleased)
      GoodsReleased.code mustEqual "IE025"
      GoodsReleased.rootNode mustEqual "CC025A"
      MessageType.arrivalValues must contain(GoodsReleased)
    }
  }

  "Json reads and writes" - {
    "writes" in {
      forAll(Gen.oneOf[MessageType](MessageType.values)) {
        messageType =>
          Json.toJson(messageType) mustEqual JsString(messageType.code)
      }
    }

    "reads" - {
      "returns the message type when given the code for a message" in {
        forAll(Gen.oneOf[MessageType](MessageType.values)) {
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
