package jkugiya.ulid

import java.nio.ByteBuffer
import java.util.UUID

private[ulid] object UUIDCodec {

  def encode(ulid: ULID): UUID = {
    val binary = ulid.binary
    val buffer = ByteBuffer.wrap(binary)
    new UUID(buffer.getLong(), buffer.getLong)
  }

  def decode(uuid: UUID): ULID = {
    val m = uuid.getMostSignificantBits
    val l = uuid.getLeastSignificantBits
    val time = m >>> 16
    val next64 = (m << 48 | l >>> 16)
    val last16 = (l & 0xFFFF).toShort
    val buffer = ByteBuffer.allocate(10)
    buffer.putLong(next64).putShort(last16)
    new ULID(time, buffer.array())
  }

}

