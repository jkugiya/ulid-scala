# ULID scala
[![Build Status](https://github.com/jkugiya/ulid-scala/workflows/CI/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/jkugiya/ulid-scala/badge.svg?branch=master)](https://coveralls.io/github/jkugiya/ulid-scala?branch=master)  

ULID (Universally Unique Lexicographically Sortable Identifier) generator and parser for Scala.

Refer [alizain/ulid](https://github.com/alizain/ulid) for a more detailed ULID specification.

## Getting Started

```
libraryDependencies += "com.github.jkugiya" %% "ulid-scala" % "<version>"
```

## Usage

ULID generation examples:

```scala
import jkugiya.ulid.ULID

val generator = ULID.getGenerator()

val ulid: ULID = generator.generate()

// or ulid.base32
val base32: String = generator.base32()

// or ulid.uuid
val uuid: UUID = generator.uuid()

// or ulid.binary
val binary: Array[Byte] = generator.binary()

```

### Monotonicity
Complete [monotonicity](https://github.com/ulid/spec#monotonicity) is not directly supported in `ulid-scala`.
However, it does have the ability to increase randomness within the same timestamp.

```scala
val generator = ULID.getGenerator()
val uuid1 = generator.uuid()
val uuid2 = uuid1.increment()

uuid1.time == uuid2.time // true
uuid1.randomness.last == uuid2.randomness.last - 1 // true
```

If strict control of the order in which ULIDs are generated is required,
keep the last ULID generated in the ID generator in the cluster and 
return the `increment` of the previous ULID if the generated ULID
conflicts with the timestamp of the previous one.

The `increment` function wraparounds randomness in the event of randomness overflow by default.
If you want to detect wraparound strictly, set the `wraparound` flag to `false` to handle the exception.

```scala
// UnsupportedOperationException thrown when wraparound.
uuid.increment(wraparound = false)
```

## LICENSE
[LICENSE](https://github.com/jkugiya/ulid-scala/blob/master/LICENSE)

## Prior Arts
- [azam/ulidj](https://github.com/azam/ulidj)
- [sulky-ulid](https://github.com/huxi/sulky/tree/master/sulky-ulid)
- [ulid4s](https://github.com/petitviolet/ulid4s)

