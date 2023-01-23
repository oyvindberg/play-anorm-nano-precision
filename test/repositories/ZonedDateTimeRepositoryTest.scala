package repositories

import com.dimafeng.testcontainers.{ForAllTestContainer, PostgreSQLContainer}
import org.scalatest.TryValues._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.play.components.OneAppPerSuiteWithComponents
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import play.api.{BuiltInComponentsFromContext, Configuration}

import java.time.{Instant, LocalDateTime, ZoneId}

class ZonedDateTimeRepositoryTest
    extends AnyFlatSpec
    with Matchers
    with OneAppPerSuiteWithComponents
    with ForAllTestContainer {
  private val dbName = "default"
  override lazy val container: PostgreSQLContainer =
    PostgreSQLContainer().configure(_.withInitScript("db-scripts/init_schema.sql"))

  override lazy val components: BuiltInComponentsFromContext with DBComponents =
    new BuiltInComponentsFromContext(context) with DBComponents with HikariCPComponents {
      override lazy val configuration: Configuration = Configuration(
        s"db.$dbName.driver" -> classOf[org.postgresql.Driver].getName,
        s"db.$dbName.url" -> container.jdbcUrl,
        s"db.$dbName.username" -> container.username,
        s"db.$dbName.password" -> container.password
      ).withFallback(context.initialConfiguration)
      override val router: Router = Router.empty
      override val httpFilters: Seq[EssentialFilter] = Seq.empty
    }

  lazy val repository: ZonedDateTimeRepository = new ZonedDateTimeRepository(components.dbApi.database(dbName))

  val zonedDateTimeWithoutNano: LocalDateTime = LocalDateTime.ofInstant(Instant.parse("2021-01-06T08:22:31.111Z"), ZoneId.systemDefault())
  val zonedDateTimeWithNano: LocalDateTime = LocalDateTime.ofInstant(Instant.parse("2021-01-06T08:22:31.222456Z"), ZoneId.systemDefault())

  it should "insert a ZonedDateTime WITHOUT nano precision and return the inserted value" in {
    val id = 1
    val addedValue: LocalDateTime = repository
      .add(id, zonedDateTimeWithoutNano)
      .success
      .value

    addedValue shouldBe zonedDateTimeWithoutNano

    val actual: LocalDateTime = repository.get(id).success.value
    actual.getNano shouldBe zonedDateTimeWithoutNano.getNano
  }

  // test fails
  it should "insert a ZonedDateTime instant WITH nano precision and return the inserted value" in {
    val id = 2
    val addedValue: LocalDateTime = repository
      .add(id, zonedDateTimeWithNano)
      .success
      .value

    addedValue shouldBe zonedDateTimeWithNano

    val actual: LocalDateTime = repository.get(id).success.value
    actual.getNano shouldBe zonedDateTimeWithNano.getNano
  }
}
