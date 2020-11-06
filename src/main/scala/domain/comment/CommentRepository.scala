package domain.comment

import cats.effect.IO

trait CommentRepository {
  def Create(comment: Comment): IO[Unit]
  def Find(commentId: CommentId): IO[Option[Comment]]
  def FindAll(): IO[List[Comment]]
}
