package jkugiya.ulid

import java.security.SecureRandom

import org.openjdk.jmh.annotations._

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
class ULIDBenchmark {

  val random = new SecureRandom()

  val gen = ULID.getGenerator(random)

  @Benchmark
  def genBase32(): Unit = {
    gen.base32()
  }

  @Benchmark
  def genUUID(): Unit = {
    gen.uuid()
  }

  @Benchmark
  def genULID(): Unit = {
    gen.generate()
  }

  // for compare
  @Benchmark
  def genRandomUUID(): Unit = {
    java.util.UUID.randomUUID().toString
  }

  import de.huxhorn.sulky.ulid.{ ULID => SULID }
  val sulid = new SULID(random)
  @Benchmark
  def sulky(): Unit = {
    sulid.nextValue().toString
  }

  import io.azam.ulidj.{ ULID => JUID }
  @Benchmark
  def julid(): Unit = {
    JUID.random(random)
  }
}
