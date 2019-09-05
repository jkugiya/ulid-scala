package jkugiya.ulid

import java.nio.ByteBuffer

import jkugiya.ulid.ULID.ByteLengthOfULID

private[ulid] object BinaryCodec {

  def encode(ulid: ULID): Array[Byte] = {
    val buffer = ByteBuffer.allocate(ByteLengthOfULID)
    buffer.putLong(ulid.time << 16) // takes 48bit only
    buffer.position(6)
    buffer.put(ulid.originalRandomness)
    buffer.array()
  }

  def decode(binary: Array[Byte]): ULID = {
    val binaryBuffer = ByteBuffer.wrap(binary)
    val m = binaryBuffer.getLong
    val l = binaryBuffer.getLong
    val time = m >>> 16
    val next64 = (m << 48 | l >>> 16)
    val last16 = (l & 0xFFFF).toShort
    val buffer = ByteBuffer.allocate(10)
    buffer.putLong(next64).putShort(last16)
    new ULID(time, buffer.array())
  }

}
