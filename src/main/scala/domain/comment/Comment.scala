package domain.comment

import cats.data.{NonEmptyList, ValidatedNel}
import cats.syntax.validated._

sealed case class Comment(Id: CommentId, Body: String) {
  def UpdateBody(body: String): Either[NonEmptyList[IllegalArgumentException], Comment] = {
    Comment.Create(Id, body)
  }
}

object Comment {
  def Create(id: CommentId, body: String): Either[NonEmptyList[IllegalArgumentException], Comment] = {
    type ValidationResult[A] = ValidatedNel[IllegalArgumentException, A]
    val validatedBody: ValidationResult[String] = {
      if (body.isEmpty) {
        new IllegalArgumentException("The body is empty").invalidNel
      } else body.validNel
    }

    validatedBody.map({Comment(id, _)}).toEither
  }
}

