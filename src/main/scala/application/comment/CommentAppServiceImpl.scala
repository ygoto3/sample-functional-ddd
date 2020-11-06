package application.comment

import cats.data.NonEmptyList
import com.google.inject.Inject
import domain.comment.{Comment, CommentService}

import scala.concurrent.Future

class CommentAppServiceImpl @Inject()(
  commentRepositoryFactory: CommentRepositoryImplFacotory
) extends CommentAppService {

  override def Create(body: String): Future[Either[NonEmptyList[Exception], Unit]] = {
    val repo = commentRepositoryFactory.NewInstance()
    CommentService.Create(body).run(repo).unsafeToFuture()
  }

  override def ViewAll(): Future[Either[NonEmptyList[Exception], List[Comment]]] = {
    val repo = commentRepositoryFactory.NewInstance()
    CommentService.ViewAll().run(repo).unsafeToFuture()
  }

  override def ViewById(id: String): Future[Either[NonEmptyList[Exception], Comment]] = {
    val repo = commentRepositoryFactory.NewInstance()
    CommentService.ViewById(id).run(repo).unsafeToFuture()
  }

}
