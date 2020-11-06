package application.comment

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.google.inject.Inject
import spray.json.DefaultJsonProtocol

import scala.util.{Failure, Success}

final case class CommentCreate(body: String)
final case class CommentResponse(id: String, body: String)
final case class ErrorResponse(message: String)

class CommentController @Inject()(
  appService: CommentAppService
) extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val CommentCreateFormat = jsonFormat1(CommentCreate)
  implicit val CommentResponseFormat = jsonFormat2(CommentResponse)
  implicit val ErrorResponseFormat = jsonFormat1(ErrorResponse)

  val Route = pathPrefix("comments") {
    pathEndOrSingleSlash {
      get {
        onComplete(appService.ViewAll()) {
          case Failure(exception) => complete(ErrorResponse(exception.getMessage))
          case Success(either) => either match {
            case Left(exceptions) => {
              complete(exceptions.map { e => ErrorResponse(e.getMessage) }.toList)
            }
            case Right(comments) => {
              complete(comments.map(c => CommentResponse(c.Id.Value, c.Body)))
            }
          }
        }
      } ~
      post {
        entity(as[CommentCreate]) { commentCreate =>
          onComplete(appService.Create(commentCreate.body)) {
            case Failure(exception) => complete(ErrorResponse(exception.getMessage))
            case Success(either) => either match {
              case Left(exceptions) => {
                complete(exceptions.map { e => ErrorResponse(e.getMessage) }.toList)
              }
              case Right(_) => {
                complete(StatusCodes.Created)
              }
            }
          }
        }
      }
    } ~
    path(".+".r) { id =>
      get {
        onComplete(appService.ViewById(id)) {
          case Failure(exception) => complete(ErrorResponse(exception.getMessage))
          case Success(either) => either match {
            case Left(exceptions) => {
              complete(exceptions.map { e => ErrorResponse(e.getMessage) }.toList)
            }
            case Right(comment) => {
              complete(CommentResponse(comment.Id.Value, comment.Body))
            }
          }
        }
      }
    }
  }

}