package spantest.game

import java.util.regex.Pattern
import scala.collection.immutable.HashMap

type GameResult = (String, Int)

case class Game(team1: Team, team2: Team)

object Game {
  def getFinalResult(games: List[Game]): String =
    val (finalResult, _, _, _) = getTeamsResult(games).foldLeft((List[String](), 0, 1, -1)) { case ((string, count, countAcc, prevPoints), (team, points)) => {
      val newCount = if prevPoints == points then count else count + countAcc
      val newCountAcc = if prevPoints == points then countAcc + 1 else 1
      (string ++ List(s"$newCount. ${team}, ${points} ${ptsString(points)}"), newCount, newCountAcc, points)
    }
    }
    finalResult.mkString("\n")
  end getFinalResult

  def getTeamsResult(games: List[Game]): List[GameResult] =
    games.foldLeft(HashMap[String, Int]()) { case (results, Game(team1, team2)) => {
      val team1Points = results.getOrElse(team1.name.trim(), 0) + calcPoints(team1.score, team2.score)
      val team2Points = results.getOrElse(team2.name.trim(), 0) + calcPoints(team2.score, team1.score)
      results + (team1.name.trim() -> team1Points) + (team2.name.trim() -> team2Points)
    }
    }.toList.sortWith { case (a, b) => a._2 > b._2 }
  end getTeamsResult


  private def ptsString(result: Int): String = if result == 1 then "pt" else "pts"

  def calcPoints(score1: Int, score2: Int): Int =
    if score1 > score2 then
      3
    else if score1 == score2 then
      1
    else 0
  end calcPoints

  def parseGames(lines: List[String]): List[Game] =
    for line <- lines
    yield {
      val Pattern = raw"^(\S.+)(\d+)(\, )(\S.+)(\d+)".r
      line match{
        case Pattern(team1Name, team1Score, comma, team2Name, team2Score) => Game(Team(team1Name, team1Score.toInt), Team(team2Name, team2Score.toInt))
      }
    }
}