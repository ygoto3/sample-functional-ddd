package application.comment

import application.RepositorySlick
import cats.effect.IO
import domain.comment.{Comment, CommentId, CommentRepository}
import slick.jdbc.MySQLProfile.api._
import slick.lifted.ProvenShape

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

sealed case class CommentRow(Id: String, Body: String)
sealed class CommentTable(tag: Tag) extends Table[CommentRow](tag, "comments") {
  def Id: Rep[String] = column[String]("id", O.PrimaryKey)
  def Body: Rep[String] = column[String]("body")
  def * : ProvenShape[CommentRow] = (Id, Body).<>(CommentRow.tupled, CommentRow.unapply)
}

trait CommentRepositoryImplFacotory {
  def NewInstance(): CommentRepositoryImpl
}

class CommentRepositorySlickFactory extends CommentRepositoryImplFacotory {
  override def NewInstance() = new CommentRepositoryImpl()
}

class CommentRepositoryImpl extends RepositorySlick with CommentRepository {
  private val query = TableQuery[CommentTable]

  override def Create(comment: Comment): IO[Unit] = {
    implicit val cs = IO.contextShift(ExecutionContext.global)
    val actions = DBIO.seq(
      query += CommentRow(comment.Id.Value, comment.Body)
    )
    IO.fromFuture(IO { db.run(actions) })
  }

  override def FindAll(): IO[List[Comment]] = {
    implicit val cs = IO.contextShift(ExecutionContext.global)
    IO.fromFuture(IO { db.run(query.result).map { _.toList.map(convert).flatten } })
  }

  override def Find(commentId: CommentId): IO[Option[Comment]] = {
    implicit val cs = IO.contextShift(ExecutionContext.global)
    val select = query.filter(_.Id === commentId.Value)
    IO.fromFuture(IO { db.run(select.result).map { _.toList.map(convert).flatten.headOption } })
  }

  private def convert(commentRow: CommentRow): Option[Comment] = {
    val eComment = for {
      id <- CommentId.Create(commentRow.Id).toEither
      comment <- Comment.Create(id, commentRow.Body)
    } yield comment
    eComment match {
      case Right(value) => Some(value)
      case _ => None
    }
  }
}
