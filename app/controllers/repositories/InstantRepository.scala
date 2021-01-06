package controllers.repositories

import play.api.db.Database
import anorm._

import java.time.Instant
import javax.inject.{Inject, Singleton}
import scala.util.Try

@Singleton
class InstantRepository @Inject() (db: Database) {

  val valueParser: RowParser[Instant] =
    SqlParser.get(name = "value")(Column.columnToInstant)

  initDatabase()

  private def initDatabase(): Unit = {
    db.withConnection { implicit c =>
      SQL"CREATE TABLE IF NOT EXISTS data (value TIMESTAMPTZ)".execute()
      SQL"""TRUNCATE TABLE data""".execute()
    }
  }
implicitly[ToParameterValue[Instant]]
  def add(value: Instant): Try[Instant] =
    db.withConnection { implicit c =>
      SQL"""
      INSERT INTO data (value) VALUES ($value) RETURNING *
       """.asTry(valueParser.single)
    }

  def list(): Try[List[Instant]] =
    db.withConnection { implicit c =>
      SQL"""
         SELECT value FROM data
       """.asTry(valueParser.*)
    }

}
