package repositories

import anorm._
import play.api.db.Database

import java.time.ZonedDateTime
import scala.util.Try

class ZonedDateTimeRepository(db: Database) {

  val valueParser: RowParser[ZonedDateTime] =
    SqlParser.get[ZonedDateTime](name = "value")(Column.columnToZonedDateTime)

  def add(id: Int, value: ZonedDateTime): Try[ZonedDateTime] =
    db.withConnection { implicit c =>
      SQL"""INSERT INTO data (id, value) VALUES ($id, $value) RETURNING value""".asTry(valueParser.single)
    }

  def get(id: Int): Try[ZonedDateTime] =
    db.withConnection { implicit c =>
      SQL"""SELECT value FROM data where id=$id""".asTry(valueParser.single)
    }

}
