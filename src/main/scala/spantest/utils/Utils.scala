package spantest.utils

import scala.io.Source
import scala.util.Using
import scala.util.Try

object Utils {
  def readFile(filename: String): Try[List[String]] = {
    Using(Source.fromFile(filename)) {source => source.getLines().toList}
  }
}
