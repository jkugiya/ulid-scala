package jkugiya.ulid

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.security.SecureRandom

class ULIDGeneratorTest extends AnyFlatSpec with Matchers {

  it should "be able to specify algorithm" in {
    // Secure Random
    val secure = SecureRandom.getInstanceStrong
    val g1 = ULID.getGenerator(secure)
    g1.algorithm() should be(secure.getAlgorithm)
    // Java's Random
    val default = new java.util.Random()
    val g2 = ULID.getGenerator(default)
    g2.algorithm() should be(default.getClass.toString)
  }
}
