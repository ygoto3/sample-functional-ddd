package application.comment

import cats.data.NonEmptyList
import domain.comment.Comment

import scala.concurrent.Future

trait CommentAppService {
  def Create(body: String): Future[Either[NonEmptyList[Exception], Unit]]
  def ViewAll(): Future[Either[NonEmptyList[Exception], List[Comment]]]
  def ViewById(id: String): Future[Either[NonEmptyList[Exception], Comment]]
}
