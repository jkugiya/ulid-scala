package jkugiya.ulid

import java.security.SecureRandom

import org.openjdk.jmh.annotations._

@State(Scope.Thread)
@BenchmarkMode(Array(Mode.Throughput))
class ULIDBenchmark {

  val random = new SecureRandom()

  val gen = ULID.getGenerator(random)

  @Benchmark
  def binary(): Unit = {
    gen.binary()
  }
  @Benchmark
  def ulid_scala_base32(): Unit = {
    gen.base32()
  }

  @Benchmark
  def ulid_scala_uuid(): Unit = {
    gen.uuid()
  }

  @Benchmark
  def uliid_scala_generate(): Unit = {
    gen.generate()
  }

  // for compare
  @Benchmark
  def java_random_uuid(): Unit = {
    java.util.UUID.randomUUID().toString
  }

  import de.huxhorn.sulky.ulid.{ ULID => SULID }
  val sulid = new SULID(random)
  @Benchmark
  def sulky_base32(): Unit = {
    sulid.nextValue().toString
  }

  import io.azam.ulidj.{ ULID => JUID }
  @Benchmark
  def julid_base32(): Unit = {
    JUID.random(random)
  }
}
