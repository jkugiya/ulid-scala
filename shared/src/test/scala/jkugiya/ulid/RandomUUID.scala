package jkugiya.ulid

import java.util.UUID
import scala.util.Random

/** 
 * Non-JVM platforms do not have `java.security.SecureRandom` which is used by `UUID.randomUUID`, thus we need a
 * replacement.
 **/
object RandomUUID {
  def apply(): UUID = new UUID(Random.nextLong(), Random.nextLong())
}
