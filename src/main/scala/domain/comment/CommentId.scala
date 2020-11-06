package domain.comment

import cats.data.ValidatedNel
import cats.syntax.validated._

sealed case class CommentId(Value: String)

object CommentId {
  def Create(value: String): ValidatedNel[IllegalArgumentException, CommentId] = {
    if (value.length != 36) new IllegalArgumentException("The length of a comment ID must be 36.").invalidNel
    else CommentId(value).validNel
  }
}
