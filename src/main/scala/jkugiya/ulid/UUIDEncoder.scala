package jkugiya.ulid

import java.nio.ByteBuffer
import java.util.UUID

private[ulid] object UUIDEncoder extends ULIDEncoder[UUID] {

  override def encode(ulid: ULID): UUID = {
    val binary = ulid.binary
    val buffer = ByteBuffer.wrap(binary)
    new UUID(buffer.getLong(), buffer.getLong)
  }

}

