package jkugiya.ulid

import java.util.{Random => JRandom}

private[ulid] object SecureGenerator {
  // Secure generation isn't widely available on JS.
  def get: JRandom = new JRandom()

  def algorithm(random: JRandom): String = random.getClass.toString
}

