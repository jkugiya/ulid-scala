package jkugiya.ulid

private[ulid] class Base32Encoder extends ULIDEncoder[String] {

  private val toBase32 = Array(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
    'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X',
    'Y', 'Z'
  )

  override def encode(ulid: ULID): String = {
    val binary = ulid.binary
    (Stream(0, 0) ++ (0 to 127).toStream.map { i =>
      val bitValueOfI = (binary(i / 8) << (i % 8)) & 0x80
      if (bitValueOfI == 0) 0
      else 1
    }).grouped(5).map { buffer =>
      val bit5 = buffer.toArray
      val index =
        (bit5(0) << 4) +
          (bit5(1) << 3) +
          (bit5(2) << 2) +
          (bit5(3) << 1) +
          bit5(4)
      toBase32(index)
    }.mkString
  }
}
