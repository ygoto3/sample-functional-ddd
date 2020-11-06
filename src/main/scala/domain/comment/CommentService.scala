package domain.comment

import cats.data.{EitherT, NonEmptyList, Reader, Validated}
import cats.effect.IO
import domain.Util

object CommentService {

  def Create(body: String): Reader[CommentRepository, IO[Either[NonEmptyList[Exception], Unit]]] =
    Reader { repo =>
      val result = for {
        uuid <- EitherT.liftF[IO, NonEmptyList[Exception], String](Util.GetUUID)
        id <- EitherT.fromEither[IO]( CommentId.Create(uuid).toEither )
        comment <- EitherT.fromEither[IO](Comment.Create(id, body))
        _ <- EitherT.liftF[IO, NonEmptyList[Exception], Unit](repo.Create(comment))
      } yield ()
      result.value
    }

  def ViewAll(): Reader[CommentRepository, IO[Either[NonEmptyList[Exception], List[Comment]]]] =
    Reader { _.FindAll().map(Right.apply) }

  def ViewById(id: String): Reader[CommentRepository, IO[Either[NonEmptyList[Exception], Comment]]] =
    Reader { repo =>
      CommentId.Create(id) match {
        case Validated.Valid(cid) => repo.Find(cid).map {
          case Some(cid) => Right(cid)
          case None => Left(NonEmptyList.one(new Exception("No comment found.")))
        }
        case Validated.Invalid(exceptions) => IO(Left(exceptions))
      }
    }
}
