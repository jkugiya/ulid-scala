package jkugiya.ulid

import java.security.SecureRandom
import java.util.{UUID, Random => JRandom}

import jkugiya.ulid.ULID._

import scala.util.Try

object ULID {

  private[ulid] val RandomnessSize = 10

  private[ulid] val ByteLengthOfULID = 16

  private[ulid] val ByteLengthOfLong = 8

  private[ulid] val MaxTimestamp = 281474976710655L
  private[ulid] val MinTimestamp = 0L

  private[ulid] final class StatefulGenerator(random: JRandom) extends ULIDGenerator {

    override final def generate(): ULID =
      new ULID(System.currentTimeMillis(), random)

    override final def algorithm: String = random match {
      case sr: SecureRandom => sr.getAlgorithm
      case _ => random.getClass.toString
    }

  }

  private[this] def secureGenerator = {
    Try(SecureRandom.getInstance("NativePRNGNonBlocking"))
      .recover({ case _ => SecureRandom.getInstanceStrong })
      .recover({ case _ => new JRandom() })
      .get
  }

  def getGenerator(random: JRandom = secureGenerator): ULIDGenerator =
    new StatefulGenerator(random)

  def fromBase32(base32: String): ULID =
    Base32Codec.decode(base32)

  def fromUUID(uuid: UUID): ULID =
    UUIDCodec.decode(uuid)

  def fromBinary(binary: Array[Byte]): ULID =
    BinaryCodec.decode(binary)

}

private[ulid] trait ULIDGenerator {

  def generate(): ULID

  final def binary(): Array[Byte] =
    BinaryCodec.encode(generate())

  final def base32(): String =
    Base32Codec.encode(generate())

  final def uuid(): UUID =
    UUIDCodec.encode(generate())

  def algorithm(): String

}

class ULID private[ulid](val time: Long, private[ulid] val originalRandomness: Array[Byte]) {
  if (time > MaxTimestamp || time < MinTimestamp) {
    throw new IllegalArgumentException(s"Invalid timestamp is given.(${time}, should be between ${MinTimestamp} to ${MaxTimestamp}.")
  }
  if (originalRandomness.length != 10) {
    val byteLength = originalRandomness.length
    val bitLength = byteLength * 8
    throw new IllegalArgumentException(s"randomness should be 80bits(10 byte), but ${bitLength} bits(${byteLength} byte) randomness is given.")
  }

  def this(time: Long, random: JRandom) = {
    this(
      time, {
        val randomness = new Array[Byte](RandomnessSize)
        random.nextBytes(randomness)
        randomness
      })
  }

  def binary: Array[Byte] = BinaryCodec.encode(this)

  def base32: String = Base32Codec.encode(this)

  def uuid: UUID = UUIDCodec.encode(this)

  def randomness: Array[Byte] = {
    val value = new Array[Byte](RandomnessSize)
    originalRandomness.copyToArray(value)
    value
  }
}

