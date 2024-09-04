package jkugiya.ulid

import java.nio.ByteBuffer

object Base32Logics {
  private val toBase32 = Array(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
    'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X',
    'Y', 'Z'
  )

  // old implementation
  def logic1(time: Long, randomness: Array[Byte]): String = {
    val ulid = new ULID(time, randomness)
    val binary = ulid.binary
    (Stream(0, 0) ++ (0 to 127).toStream.map { i =>
      val bitValueOfI = (binary(i / 8) << (i % 8)) & 0x80
      if (bitValueOfI == 0) 0
      else 1
    }).grouped(5).map { buffer =>
      val bit5 = buffer.toArray
      val index =
        (bit5(0) << 4) |
          (bit5(1) << 3) |
          (bit5(2) << 2) |
          (bit5(3) << 1) |
          bit5(4)
      toBase32(index)
    }.mkString
  }

  // old implementation
  val MaskForTake5 = 0xf800000000000000L
  def logic2(time: Long, randomness: Array[Byte]): String = {
    val ulid = new ULID(time, randomness)
    val binary = ulid.binary // len = 16
    val chars = new Array[Char](26)
    val mostSigBits = ByteBuffer.wrap(binary.slice(0, 8)).getLong
    val leastSigBits = ByteBuffer.wrap(binary.slice(8, 16)).getLong
    var i = 0
    // first 60bits(empty 2bits + 58bits)
    val firstBits = mostSigBits >>> 6 << 4
    while(i < 12) {
      chars(i) = toBase32(((firstBits << (i * 5) & MaskForTake5) >>> 59).toInt)
      i += 1
    }
    // second 60bits
    val secondBits = ((mostSigBits << 58) | (leastSigBits >>> 10 << 4))
    i = 12
    while(i < 24) {
      chars(i) = toBase32(((secondBits << (i - 12) * 5 & MaskForTake5) >>> 59).toInt)
      i += 1
    }
    // third 10 bits
    i = 24
    val thirdBits = leastSigBits << 54
    while(i < 26) {
      chars(i) = toBase32(((thirdBits << (i - 24) * 5 & MaskForTake5) >>> 59).toInt)
      i += 1
    }
    chars.mkString
  }
}
