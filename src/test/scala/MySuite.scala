import org.scalacheck.Gen
import org.scalacheck.Prop.*

import scala.util.{Failure, Success, Try}
import spantest.game.{Game, Team}
import spantest.utils.Utils

class MySuite extends munit.ScalaCheckSuite {
  val resultString =
    """1. Tarantulas, 6 pts
      |2. Lions, 5 pts
      |3. FC Awesome, 1 pt
      |3. Snakes, 1 pt
      |5. Grouches, 0 pts""".stripMargin

  val simpleTeamGenerator: Gen[Team] = for
    points <- Gen.posNum[Int]
    name <- Gen.alphaStr
    if name.nonEmpty
  yield Team(name, points)

  val simpleGameGenerator: Gen[Game] = for
    team1 <- simpleTeamGenerator
    team2 <- simpleTeamGenerator
    if team1.name != team2.name
  yield Game(team1, team2)

  property("points calculation should correct") {
    forAll(simpleGameGenerator) { (game: Game) => {
      val points = Game.calcPoints(game.team1.score, game.team2.score)
      if game.team1.score > game.team2.score then
        assertEquals(points, 3)
      else if game.team1.score < game.team2.score then
        assertEquals(points, 0)
      else
        assertEquals(points, 1)
    }}
  }

  property("number of teams should match") {
    forAll(Gen.listOfN(10, simpleGameGenerator)) { (games: List[Game]) => {
      val teamNames = games.flatMap(game => List(game.team1.name) ++ List(game.team2.name)).toSet
      val results = Game.getTeamsResult(games).map(_._1).toSet
      teamNames == results
    }}
  }

  test("calculated string should match with the result string") {
    val game1 = Game(Team("Lions", 3), Team("Snakes", 3))
    val game2 = Game(Team("Tarantulas", 1), Team("FC Awesome", 0))
    val game3 = Game(Team("Lions", 1), Team("FC Awesome", 1))
    val game4 = Game(Team("Tarantulas", 3), Team("Snakes", 1))
    val game5 = Game(Team("Lions", 4), Team("Grouches", 0))

    val result = Game.getFinalResult(List(game1, game2, game3, game4, game5))
    assertEquals(result, resultString)
  }

  test("should process correctly input example files") {
    val res = for
      lines <- Utils.readFile("./src/test/resources/input.txt")
      games <- Try(Game.parseGames(lines))
      result <- Try(Game.getFinalResult(games))
    yield result
    assertEquals(res, Success(resultString))
  }

  test("should fail if the input file does not exist") {
    val res = for
      lines <- Utils.readFile("./src/main/resources/input-fail.txt")
      games <- Try(Game.parseGames(lines))
      result <- Try(Game.getFinalResult(games))
    yield result
    assertEquals(res.isFailure, true)
  }
}
