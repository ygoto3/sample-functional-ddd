package application

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import application.comment._
import cats.effect.IO
import com.google.inject.{AbstractModule, Guice}
import net.codingwell.scalaguice.InjectorExtensions.ScalaInjector
import net.codingwell.scalaguice.ScalaModule

import scala.io.StdIn

class MyModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[CommentAppService].to[CommentAppServiceImpl]
    bind[CommentRepositoryImplFacotory].to[CommentRepositorySlickFactory]
    bind[CommentController]
  }
}

object Gateway {
  private val injector = Guice.createInjector(new MyModule)

  def Route(): IO[Unit] = IO {

    implicit val system = ActorSystem(Behaviors.empty, "gateway-system")
    implicit val executionContext = system.executionContext

    val route =
      pathPrefix("v1") {
        injector.instance[CommentController].Route
      }

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}