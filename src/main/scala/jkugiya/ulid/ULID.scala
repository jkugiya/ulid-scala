package jkugiya.ulid

import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.{ UUID, Random => JRandom }

import jkugiya.ulid.ULID._

import scala.util.Try

object ULID {

  private[ulid] val RandomnessSize = 10

  private[ulid] val ByteLengthOfULID = 16

  private[ulid] val ByteLengthOfLong = 8

  private[ulid] final class StatefulGenerator(random: JRandom) extends ULIDGenerator {

    def generate(): ULID =
      new ULID(System.currentTimeMillis(), random)

  }

  private[this] def secureGenerator = {
    Try(SecureRandom.getInstance("NativePRNGNonBlocking"))
      .recover({ case _ => SecureRandom.getInstanceStrong })
      .get
  }

  def getGenerator(random: JRandom = secureGenerator): ULIDGenerator =
    new StatefulGenerator(random)

}

trait ULIDGenerator {

  def generate(): ULID

  final def generateValue[A: ULIDEncoder]: A =
    implicitly[ULIDEncoder[A]].encode(generate())

  final def base32(): String =
    ULIDEncoder.base32Encoder.encode(generate())

  final def uuid(): UUID =
    ULIDEncoder.uuidEncoder.encode(generate())

}

trait ULIDEncoder[A] {
  def encode(ulid: ULID): A
}

object ULIDEncoder {
  implicit val base32Encoder: ULIDEncoder[String] = new Base32Encoder
  implicit val uuidEncoder: ULIDEncoder[UUID] = new UUIDEncoder
}

private[ulid] class ULID(val time: Long, private[ulid] val originalRandomness: Array[Byte]) {

  def this(time: Long, random: JRandom) = {
    this(
      time, {
        val randomness = new Array[Byte](RandomnessSize)
        random.nextBytes(randomness)
        randomness
      })
  }

  def binary: Array[Byte] = {
    val buffer = ByteBuffer.allocate(ByteLengthOfULID)
    val timeArray =
      ByteBuffer
        .allocate(ByteLengthOfLong).putLong(time).array()
        .drop(2) // (64bit(long) - 48bit(timestamp length of ULID)) = 16bit = 2byte
    buffer.put(timeArray)
    buffer.put(originalRandomness)
    buffer.array()
  }

  def randomness: Array[Byte] = {
    val value = new Array[Byte](RandomnessSize)
    originalRandomness.copyToArray(value)
    value
  }
}
