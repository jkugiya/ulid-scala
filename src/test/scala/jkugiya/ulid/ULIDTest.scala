package jkugiya.ulid


import org.scalatest.{ FlatSpec, Matchers }

import scala.util.Random

class ULIDTest extends FlatSpec with Matchers {

  def ulid(timestamp: Long, randomness: Array[Byte]): ULID =
    new ULID(timestamp, randomness)

  private def nextBytes(n: Int = 10) = {
    val bytes = new Array[Byte](0 max n)
    Random.nextBytes(bytes)
    bytes
  }
  private def nextLong(): Long = {
    val value = Random.nextLong()
    if (value < 0 || value >= ULID.MaxTimestamp) nextLong()
    else value
  }

  "first 48bit" should "be generated from timestamp" in {
    val randomness = nextBytes()
    ulid(ULID.MinTimestamp, randomness).base32.take(10) shouldBe "0" * 10
    ulid(1L, randomness).base32.take(10) shouldBe "0" * 9 + "1"
    ulid(ULID.MaxTimestamp, randomness).base32.take(10) shouldBe "7ZZZZZZZZZ"
  }
  "last 80bit" should "be generated from randomness" in {
    val timestamp = nextLong()
    ulid(timestamp, Array.fill(10)(0)).base32.takeRight(16) shouldBe "0" * 16
    ulid(timestamp, Array.fill(10)(-1)).base32.takeRight(16) shouldBe "Z" * 16
  }
  it should "be failed" in {
    an[IllegalArgumentException] should be thrownBy ulid(ULID.MinTimestamp - 1, nextBytes())
    an[IllegalArgumentException] should be thrownBy ulid(ULID.MaxTimestamp + 1, nextBytes())
    an[IllegalArgumentException] should be thrownBy ulid(ULID.MinTimestamp, nextBytes(9))
    an[IllegalArgumentException] should be thrownBy ulid(ULID.MinTimestamp, nextBytes(11))
  }
}
