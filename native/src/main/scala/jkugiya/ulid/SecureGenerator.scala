package jkugiya.ulid

import java.util.{Random => JRandom}

private[ulid] object SecureGenerator {
  // Secure generation isn't available on native.
  def get: JRandom = new JRandom()
}

