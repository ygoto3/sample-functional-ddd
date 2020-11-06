import application.Gateway
import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    Gateway.Route().as(ExitCode.Success)

}
