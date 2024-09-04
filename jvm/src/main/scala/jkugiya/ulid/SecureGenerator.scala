package jkugiya.ulid

import java.security.SecureRandom
import java.util.{Random => JRandom}
import scala.util.Try

private[ulid] object SecureGenerator {
  def get: JRandom = {
    Try(SecureRandom.getInstance("NativePRNGNonBlocking"))
      .recover({ case _ => SecureRandom.getInstanceStrong })
      .recover({ case _ => new JRandom() })
      .get
  }
}
