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

package metrics

object MetricsKeys {

  object Messages {
    val MessageSize             = "message-size"
    val NumberOfGoods           = "number-of-goods"
    val NumberOfDocuments       = "number-of-documents"
    val NumberOfSpecialMentions = "number-of-special-mentions"
    val NumberOfSeals           = "number-of-seals"
  }

  object Endpoints {
    val HandleMessage = "incoming-router-handle-message"
  }

  object Connectors {
    val RouteToDepartures = "incoming-router-to-departure"
    val RouteToArrivals   = "incoming-router-to-destination"
    val RouteToGuarantee  = "incoming-router-to-guarantee"
  }
}
