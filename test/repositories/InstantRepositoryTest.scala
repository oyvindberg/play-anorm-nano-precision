package repositories

import org.scalatest.{Inspectors, TryValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

import java.time.Instant

class InstantRepositoryTest
    extends AnyFlatSpec
    with Matchers
    with GuiceOneAppPerSuite
    with TryValues
    with Inspectors {

  val repository: InstantRepository = app.injector.instanceOf[InstantRepository]

  val instantWithoutNano: Instant = Instant.parse("2021-01-06T08:22:31.111Z")
  val instantWithNano: Instant = Instant.parse("2021-01-06T08:22:31.222456Z")

  "add()" should "insert a java8 instant WITHOUT nano precision, returning inserted value" in {
    val addedValue: Instant = repository
      .add(instantWithoutNano)
      .success
      .value

    // ok, no problem without nanosecods
    addedValue shouldBe instantWithoutNano
  }

  it should "insert a java8 instant WITH nano precision, returning inserted value" in {
    val addedValue: Instant = repository
      .add(instantWithNano)
      .success
      .value

    // fail, because of the reported issue
    addedValue shouldBe instantWithNano
  }

  "list()" should "read java8 instant with nano precision" in {
    val allValues: List[Instant] = repository.list().success.value

    // ok, no problem without nanoseconds
    forExactly(1, allValues) { instant =>
      instant.getNano shouldBe instantWithoutNano.getNano
    }

    // fail, because of the reported issue
    forExactly(1, allValues) { instant =>
      instant.getNano shouldBe instantWithNano.getNano
    }
  }

}
