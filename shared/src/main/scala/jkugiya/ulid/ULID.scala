package jkugiya.ulid

import jkugiya.ulid.ULID._

import java.nio.ByteBuffer
import java.util.{UUID, Random => JRandom}
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

    override final def algorithm(): String = SecureGenerator.algorithm(random)
  }

  def getGenerator(random: JRandom = SecureGenerator.get): ULIDGenerator =
    new StatefulGenerator(random)

  def fromBase32(base32: String): ULID =
    Base32Codec.decode(base32)

  def fromUUID(uuid: UUID): ULID =
    UUIDCodec.decode(uuid)

  def fromBinary(binary: Array[Byte]): ULID =
    BinaryCodec.decode(binary)

}

trait ULIDGenerator {

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

  /**
   * Increments randomness.
   * (For more information on the the terms, please refer [[https://github.com/ulid/spec#specification specification]].)
   * @param wraparound if `true`, randomness wraparound when overflowing. Default is `true`.
   * @return uild with increased randomness
   */
  def increment(wraparound: Boolean = true): ULID = {
    val bf = ByteBuffer.wrap(randomness)
    val msb = bf.getShort
    val lsb = bf.getLong
    if (lsb != -1L/* 0xFFFF_FFFF_FFFF_FFFF*/) {
      val newBf = ByteBuffer.allocate(10)
      val newRandomNess = newBf.putShort(msb).putLong(lsb + 1).array()
      new ULID(time, newRandomNess)
    } else if (msb != -1 /** 0xFFFF */) {
      val newBf = ByteBuffer.allocate(10)
      val newRandomNess = newBf.putShort((msb + 1).toShort).putLong(0).array()
      new ULID(time, newRandomNess)
    } else if (wraparound) {
      new ULID(time, new Array[Byte](10))
    } else {
      throw new UnsupportedOperationException("Randomness of ULID has overflowed.")
    }
  }

  override def equals(obj: Any): Boolean = obj match {
    case other: ULID =>
      (time == other.time) && (originalRandomness sameElements other.originalRandomness)
    case _ =>
      false
  }

}

