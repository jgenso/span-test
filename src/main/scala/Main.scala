import spantest.utils.Utils
import spantest.game.Game
import scala.util.{Try, Success, Failure}
@main def games(filename: String): Unit =
  val res = for
    lines <- Utils.readFile(filename)
    games <- Try(Game.parseGames(lines))
    result <- Try(Game.getFinalResult(games))
  yield result
  res match {
    case Success(value) => println(value)
    case Failure(e) => println(s"Error: ${e.getMessage}")
  }
