package jkugiya.ulid

import java.nio.ByteBuffer
import java.util.UUID

private[ulid] object UUIDEncoder extends ULIDEncoder[UUID] {

  override def encode(ulid: ULID): UUID = {
    val binary = ulid.binary
    val mostSigBits = ByteBuffer.wrap(binary.slice(0, 8)).getLong
    val leastSigBits = ByteBuffer.wrap(binary.slice(8, 16)).getLong
    new UUID(mostSigBits, leastSigBits)
  }

}

