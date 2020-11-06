package domain

import java.util.UUID.randomUUID

import cats.effect.IO

object Util {
  def GetUUID: IO[String] = IO { randomUUID.toString }
}
