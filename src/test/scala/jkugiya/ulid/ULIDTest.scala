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
    ulid(timestamp, Array.fill(10)(255.toByte)).base32.takeRight(16) shouldBe "Z" * 16
  }
  "base32" should "be equal to another implementation" in {
    (1 to 100).foreach { _ =>
      val timestamp = System.currentTimeMillis()
      val randomness = nextBytes()
      val base32 = ulid(timestamp, randomness).base32
      base32 shouldBe (Base32Logics.logic1(timestamp, randomness))
      base32 shouldBe (Base32Logics.logic2(timestamp, randomness))
    }
  }
  it should "be failed" in {
    an[IllegalArgumentException] should be thrownBy ulid(ULID.MinTimestamp - 1, nextBytes())
    an[IllegalArgumentException] should be thrownBy ulid(ULID.MaxTimestamp + 1, nextBytes())
    an[IllegalArgumentException] should be thrownBy ulid(ULID.MinTimestamp, nextBytes(9))
    an[IllegalArgumentException] should be thrownBy ulid(ULID.MinTimestamp, nextBytes(11))
  }
  "generated Base32" should "be equal to decoded value" in {
    val gen = ULID.getGenerator()
    val base32 = gen.base32()
    val decoded = ULID.fromBase32(base32)
    base32 shouldBe(decoded.base32)
  }
  "generated UUID" should "be equal to decoded value" in {
    val gen = ULID.getGenerator()
    val uuid = gen.uuid()
    val decoded = ULID.fromUUID(uuid)
    uuid shouldBe(decoded.uuid)
  }
  "generated binary" should "be equal to decoded value" in {
    val gen = ULID.getGenerator()
    val binary = gen.binary()
    val decoded = ULID.fromBinary(binary)
    binary shouldBe(decoded.binary)
  }

  "randomness" should "be copied original" in {
    val gen = ULID.getGenerator()
    val uuid = gen.generate()
    (uuid.originalRandomness zip uuid.randomness).foreach {
      case (l, r) => l shouldBe(r)
    }
  }
  it should "not decode invalid character" in {
    an[IllegalArgumentException] should be thrownBy ULID.fromBase32("abc")
    an[IllegalArgumentException] should be thrownBy ULID.fromBase32("=" * 26)
  }
  it should "not decode invalid binary" in {
    an[IllegalArgumentException] should be thrownBy ULID.fromBinary(new Array[Byte](0))
    an[IllegalArgumentException] should be thrownBy ULID.fromBinary(new Array[Byte](15))
    an[IllegalArgumentException] should be thrownBy ULID.fromBinary(new Array[Byte](17))
  }
}
