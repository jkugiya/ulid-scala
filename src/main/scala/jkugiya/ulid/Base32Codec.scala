package jkugiya.ulid

import java.nio.ByteBuffer

private[ulid] object Base32Codec {

  private val toBase32 = Array(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
    'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X',
    'Y', 'Z'
  )

  def encode(ulid: ULID): String = {
    val chars = new Array[Char](26)
    // timestamp(10byte)
    // take last 5bit
    val timestamp = ulid.time
    chars(9) = toBase32((timestamp & 0x1F).toInt)
    chars(8) = toBase32((timestamp >>> 5 & 0x1F).toInt)
    chars(7) = toBase32((timestamp >>> 10 & 0x1F).toInt)
    chars(6) = toBase32((timestamp >>> 15 & 0x1F).toInt)
    chars(5) = toBase32((timestamp >>> 20 & 0x1F).toInt)
    chars(4) = toBase32((timestamp >>> 25 & 0x1F).toInt)
    chars(3) = toBase32((timestamp >>> 30 & 0x1F).toInt)
    chars(2) = toBase32((timestamp >>> 35 & 0x1F).toInt)
    chars(1) = toBase32((timestamp >>> 40 & 0x1F).toInt)
    chars(0) = toBase32((timestamp >>> 45 & 0x1F).toInt)
    // randomness(16byte / 80bits)
    // take 5bit
    val randomness = ulid.originalRandomness
    chars(10) = toBase32(randomness(0) >>> 3 & 0x1F)
    chars(11) = toBase32(((randomness(0) << 2) | (randomness(1) >>> 6)) & 0x1F)
    chars(12) = toBase32((randomness(1) >>> 1) & 0x1F)
    chars(13) = toBase32(((randomness(1) << 4) | (randomness(2) >>> 4)) & 0x1F)
    chars(14) = toBase32(((randomness(2) << 5) | (randomness(3) >>> 7)) & 0x1F)
    chars(15) = toBase32((randomness(3) >>> 2) & 0x1F)
    chars(16) = toBase32(((randomness(3) << 3) | (randomness(4) >>> 5)) & 0x1F)
    chars(17) = toBase32(randomness(4) & 0x1F)
    chars(18) = toBase32(randomness(5) >>> 3 & 0x1F)
    chars(19) = toBase32(((randomness(5) << 2) | (randomness(6) >>> 6)) & 0x1F)
    chars(20) = toBase32((randomness(6) >>> 1) & 0x1F)
    chars(21) = toBase32(((randomness(6) << 4) | (randomness(7) >>> 4)) & 0x1F)
    chars(22) = toBase32(((randomness(7) << 5) | (randomness(8) >>> 7)) & 0x1F)
    chars(23) = toBase32((randomness(8) >>> 2) & 0x1F)
    chars(24) = toBase32(((randomness(8) << 3) | (randomness(9) >>> 5)) & 0x1F)
    chars(25) = toBase32(randomness(9) & 0x1F)
    new String(chars)
  }

  def decode(base32: String): ULID = {
    if (base32.length != 26) {
      throw new IllegalArgumentException("The length of Base32 string should be 26")
    }
    val chars = base32.toCharArray
    var i = 0
    var time = 0L
    var next40 = 0L
    var last40 = 0L
    def bit = {
      val bit = toBase32.indexOf(chars(i))
      if (bit < 0) {
        throw new IllegalArgumentException(s"Given string contains invalid character. (${chars(i)})")
      }
      bit
    }
    while(i < 10) {
      time = (time << 5) | bit
      i += 1
    }
    while (i < 18) {
      next40 = (next40 << 5) | bit
      i += 1
    }
    while(i < 26) {
      last40 = (last40 << 5) | bit
      i += 1
    }
    val first64 = next40 << 24 | (last40 >>> 16)
    val last16: Short = (last40 & 0xFFFF).toShort
    val buffer = ByteBuffer.allocate(10)
    buffer.putLong(first64).putShort(last16)
    val ulid = new ULID(time, buffer.array())
    ulid
  }
}
