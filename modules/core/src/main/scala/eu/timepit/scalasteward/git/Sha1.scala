/*
 * Copyright 2018 scala-steward contributors
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

package eu.timepit.scalasteward.git

import cats.Eq
import cats.implicits._
import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.boolean.{And, Or}
import eu.timepit.refined.char.Digit
import eu.timepit.refined.collection.{Forall, Size}
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric.Interval
import eu.timepit.scalasteward.git.Sha1.HexString
import io.circe.refined._
import io.circe.{Decoder, Encoder}

final case class Sha1(value: HexString)

object Sha1 {
  type HexDigit = Digit Or Interval.Closed[W.`'a'`.T, W.`'f'`.T]
  type HexString = String Refined (Forall[HexDigit] And Size[Equal[W.`40`.T]])
  object HexString extends RefinedTypeOps[HexString, String]

  implicit val sha1Eq: Eq[Sha1] =
    Eq.by(_.value.value)

  implicit val sha1Decoder: Decoder[Sha1] =
    Decoder[HexString].map(Sha1.apply)

  implicit val sha1Encoder: Encoder[Sha1] =
    Encoder[HexString].contramap(_.value)
}
