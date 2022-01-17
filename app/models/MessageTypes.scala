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

package models

import scala.xml.NodeSeq

sealed abstract class MessageType(val code: String, val rootNode: String)

sealed abstract class ArrivalMessage(override val code: String, override val rootNode: String)   extends MessageType(code, rootNode)
sealed abstract class DepartureMessage(override val code: String, override val rootNode: String) extends MessageType(code, rootNode)
sealed abstract class GuaranteeMessage(override val code: String, override val rootNode: String) extends MessageType(code, rootNode)
sealed abstract class ErrorMessage(override val code: String, override val rootNode: String)     extends MessageType(code, rootNode)

object MessageType {
  case object ArrivalRejection          extends ArrivalMessage("IE008", "CC008A")
  case object UnloadingPermission       extends ArrivalMessage("IE043", "CC043A")
  case object UnloadingRemarksRejection extends ArrivalMessage("IE058", "CC058A")
  case object GoodsReleased             extends ArrivalMessage("IE025", "CC025A")

  case object PositiveAcknowledgement     extends DepartureMessage("IE928", "CC928A")
  case object MrnAllocated                extends DepartureMessage("IE028", "CC028A")
  case object DeclarationRejected         extends DepartureMessage("IE016", "CC016A")
  case object ControlDecisionNotification extends DepartureMessage("IE060", "CC060A")
  case object NoReleaseForTransit         extends DepartureMessage("IE051", "CC051B")
  case object ReleaseForTransit           extends DepartureMessage("IE029", "CC029B")
  case object CancellationDecision        extends DepartureMessage("IE009", "CC009A")
  case object WriteOffNotification        extends DepartureMessage("IE045", "CC045A")
  case object GuaranteeNotValid           extends DepartureMessage("IE055", "CC055A")

  case object ResponseQueryOnGuarantees extends GuaranteeMessage("IE037", "CD037A")

  case object XMLSubmissionNegativeAcknowledgement extends ErrorMessage("IE917", "CC917A")
  case object FunctionalNegativeAcknowledgement    extends ErrorMessage("IE906", "CD906A")

  val departureValues: Set[DepartureMessage] = Set(
    PositiveAcknowledgement,
    MrnAllocated,
    DeclarationRejected,
    ControlDecisionNotification,
    NoReleaseForTransit,
    ReleaseForTransit,
    CancellationDecision,
    WriteOffNotification,
    GuaranteeNotValid
  )

  val arrivalValues: Set[ArrivalMessage] =
    Set(ArrivalRejection, UnloadingPermission, UnloadingRemarksRejection, GoodsReleased)

  val guaranteeValues: Set[GuaranteeMessage] =
    Set(ResponseQueryOnGuarantees)

  val nctsMonitoringDepartureValues: Set[MessageType] =
    Set(MrnAllocated, DeclarationRejected, CancellationDecision, PositiveAcknowledgement,
      XMLSubmissionNegativeAcknowledgement, FunctionalNegativeAcknowledgement)

  val errorValues: Set[ErrorMessage] = Set(XMLSubmissionNegativeAcknowledgement, FunctionalNegativeAcknowledgement)

  val validMessages: Set[MessageType] = departureValues ++ arrivalValues ++ guaranteeValues ++ errorValues

  def fromHeaderValue(headerValue: String): Option[MessageType] =
    validMessages.find(_.code == headerValue)

  def getMessageType(nodeSeq: NodeSeq): Option[MessageType] =
    nodeSeq.headOption.flatMap(
      head =>
        validMessages.find(
          x => x.rootNode == head.label
        )
    )
}
