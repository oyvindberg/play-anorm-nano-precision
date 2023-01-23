package repositories

import anorm._
import play.api.db.Database

import java.time.LocalDateTime
import scala.util.Try

class ZonedDateTimeRepository(db: Database) {

  val valueParser: RowParser[LocalDateTime] =
    SqlParser.get[LocalDateTime](name = "value")(Column.columnToLocalDateTime)

  def add(id: Int, value: LocalDateTime): Try[LocalDateTime] =
    db.withConnection { implicit c =>
      SQL"""INSERT INTO data (id, value) VALUES ($id, $value) RETURNING value""".asTry(valueParser.single)
    }

  def get(id: Int): Try[LocalDateTime] =
    db.withConnection { implicit c =>
      SQL"""SELECT value FROM data where id=$id""".asTry(valueParser.single)
    }

}
