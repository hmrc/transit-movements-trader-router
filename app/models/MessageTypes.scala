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

import scala.xml.NodeSeq
import cats.data.ReaderT


sealed trait MessageType extends IeMetadata {
  def code: String
  def rootNode: String
}

object MessageType extends Enumerable.Implicits {

  case object ArrivalNotification       extends IeMetadata("IE007", "CC007A") with MessageType
  case object ArrivalRejection          extends IeMetadata("IE008", "CC008A") with MessageType
  case object UnloadingPermission       extends IeMetadata("IE043", "CC043A") with MessageType
  case object UnloadingRemarks          extends IeMetadata("IE044", "CC044A") with MessageType
  case object UnloadingRemarksRejection extends IeMetadata("IE058", "CC058A") with MessageType
  case object GoodsReleased             extends IeMetadata("IE025", "CC025A") with MessageType

  case object PositiveAcknowledgement        extends IeMetadata("IE928", "CC928A") with MessageType
  case object DepartureDeclaration           extends IeMetadata("IE015", "CC015B") with MessageType
  case object MrnAllocated                   extends IeMetadata("IE028", "CC028A") with MessageType
  case object DeclarationRejected            extends IeMetadata("IE016", "CC016A") with MessageType
  case object ControlDecisionNotification    extends IeMetadata("IE060", "CC060A") with MessageType
  case object NoReleaseForTransit            extends IeMetadata("IE051", "CC051A") with MessageType
  case object ReleaseForTransit              extends IeMetadata("IE029", "CC029A") with MessageType
  case object DeclarationCancellationRequest extends IeMetadata("IE014", "CC014A") with MessageType
  case object CancellationDecision           extends IeMetadata("IE009", "CC009A") with MessageType
  case object WriteOffNotification           extends IeMetadata("IE045", "CC045A") with MessageType
  case object GuaranteeNotValid              extends IeMetadata("IE055", "CC055A") with MessageType

  val departureValues: Seq[MessageType] = Seq(PositiveAcknowledgement, DepartureDeclaration, MrnAllocated, DeclarationRejected, ControlDecisionNotification, NoReleaseForTransit,
    ReleaseForTransit, DeclarationCancellationRequest, CancellationDecision, WriteOffNotification, GuaranteeNotValid)

  val arrivalValues: Seq[MessageType] = Seq(ArrivalNotification, ArrivalRejection, UnloadingPermission, UnloadingRemarks, UnloadingRemarksRejection, GoodsReleased)

  val values: Seq[MessageType] = departureValues ++ arrivalValues

  def getMessageType: ReaderT[Option, NodeSeq, MessageType] =
    ReaderT[Option, NodeSeq, MessageType] {
      nodeSeq =>
        values.find(_.rootNode == nodeSeq.head.label)
    }

  implicit val enumerable: Enumerable[MessageType] =
    Enumerable(values.map(v => v.code -> v): _*)
}