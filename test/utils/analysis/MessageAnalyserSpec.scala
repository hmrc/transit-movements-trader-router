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

package utils.analysis

import com.codahale.metrics.Histogram
import data.TestXml
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.mockito.MockitoSugar
import utils.TestMetrics

class MessageAnalyserSpec extends AnyFreeSpec with Matchers with MockitoSugar with TestXml with BeforeAndAfterEach {

  private val analyser = new MessageAnalyser(new TestMetrics) {
    override lazy val messageSize: Histogram             = mock[Histogram]
    override lazy val numberOfGoods: Histogram           = mock[Histogram]
    override lazy val numberOfDocuments: Histogram       = mock[Histogram]
    override lazy val numberOfSpecialMentions: Histogram = mock[Histogram]
    override lazy val numberOfSeals: Histogram           = mock[Histogram]
  }

  private val trackedXmlMovements   = Seq(CC051B, CC043A, CC029B)
  private val untrackedXmlMovements = Seq(CC060A, CC009A)

  def resetMocks()                          = reset(analyser.messageSize, analyser.numberOfGoods, analyser.numberOfDocuments, analyser.numberOfSpecialMentions, analyser.numberOfSeals)
  override protected def beforeEach(): Unit = resetMocks()

  "MessageAnalyser" - {

    "trackMessageStats" - {

      "must only return size if message is not a UnloadingPermission, NoReleaseForTransit or ReleaseForTransit" in {
        untrackedXmlMovements.foreach {
          xml =>
            resetMocks()
            analyser.trackMessageStats(xml)

            // messageSize should be called once
            verify(analyser.messageSize, times(1)).update(anyInt())

            // everything else shouldnt be called
            verifyNoInteractions(analyser.numberOfGoods)
            verifyNoInteractions(analyser.numberOfDocuments)
            verifyNoInteractions(analyser.numberOfSpecialMentions)
            verifyNoInteractions(analyser.numberOfSeals)
        }
      }

      "must be called the appropriate number of times for an UnloadingPermission (CC043A)" in {
        analyser.trackMessageStats(CC043A)

        verify(analyser.messageSize, times(1)).update(anyInt())
        verify(analyser.numberOfGoods, times(1)).update(1)
        verify(analyser.numberOfDocuments, times(1)).update(1)
        verify(analyser.numberOfSpecialMentions, times(1)).update(1)
        verify(analyser.numberOfSeals, times(1)).update(1)

      }

      "must be called the appropriate number of times for a UnloadingPermission with no documents or special mentions (CC043AWithNoDocumentsOrMentions)" in {
        analyser.trackMessageStats(CC043AWithNoDocumentsOrMentions)

        verify(analyser.messageSize, times(1)).update(anyInt())
        verify(analyser.numberOfGoods, times(1)).update(1)
        verify(analyser.numberOfDocuments, times(1)).update(0)
        verify(analyser.numberOfSpecialMentions, times(1)).update(0)
        verify(analyser.numberOfSeals, times(1)).update(1)

      }

      "must be called the appropriate number of times for an NoReleaseForTransit (CC051B)" in {
        analyser.trackMessageStats(CC051B)

        verify(analyser.messageSize, times(1)).update(anyInt())
        verify(analyser.numberOfGoods, times(1)).update(1)
        verify(analyser.numberOfDocuments, times(1)).update(0)
        verify(analyser.numberOfSpecialMentions, times(1)).update(0)
        verify(analyser.numberOfSeals, times(1)).update(0)

      }

      "must be called the appropriate number of times for an NoReleaseForTransit (CC051BWithMultipleGoodsItems) with multiple goods items" in {
        analyser.trackMessageStats(CC051BWithMultipleGoodsItems)

        verify(analyser.messageSize, times(1)).update(anyInt())
        verify(analyser.numberOfGoods, times(1)).update(3)

        verify(analyser.numberOfDocuments, times(3)).update(0)

        verify(analyser.numberOfSpecialMentions, times(3)).update(0)
        verify(analyser.numberOfSeals, times(1)).update(0)

      }

      "must be called the appropriate number of times for a ReleaseForTransit (CC029B)" in {
        analyser.trackMessageStats(CC029B)

        verify(analyser.messageSize, times(1)).update(anyInt())
        verify(analyser.numberOfGoods, times(1)).update(1)
        verify(analyser.numberOfDocuments).update(0)
        verify(analyser.numberOfSpecialMentions).update(1)
        verify(analyser.numberOfSeals, times(1)).update(1)

      }

    }

  }
}
