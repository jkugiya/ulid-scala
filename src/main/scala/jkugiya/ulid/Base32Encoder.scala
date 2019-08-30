package jkugiya.ulid

import java.nio.ByteBuffer

private[ulid] object Base32Encoder extends ULIDEncoder[String] {

  private val toBase32 = Array(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
    'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X',
    'Y', 'Z'
  )

  private val MaskForTake5 = 0xf800000000000000L

  override def encode(ulid: ULID): String = {
    // convert 130bits(empty 2bits + 128bit) to base32 26 characters
    val binary = ulid.binary // len = 16
    val chars = new Array[Char](26)
    val mostSigBits = ByteBuffer.wrap(binary.slice(0, 8)).getLong
    val leastSigBits = ByteBuffer.wrap(binary.slice(8, 16)).getLong
    var i = 25
    // last 60-1bits
    while(i > 13) {
      chars(i) = toBase32((leastSigBits >>> ((25 - i) * 5) & 0x1F).toInt)
      i += -1
    }
    // last 120-60 bits
    var bitsBuffer = (leastSigBits >>> 60) | (mostSigBits << 4)
    while(i > 1) {
      chars(i) = toBase32((bitsBuffer >>> ((13 - i) * 5) & 0x1F).toInt)
      i += -1
    }
    // first 10 bits(include empty 2bits)
    bitsBuffer = mostSigBits >>> 56
    while(i > -1) {
      chars(i) = toBase32((bitsBuffer >>> ((1 - i) * 5) & 0x1F).toInt)
      i += -1
    }
    new String(chars)
  }
}
