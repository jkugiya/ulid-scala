# ULID scala
[![Build Status](https://github.com/jkugiya/ulid-scala/workflows/CI/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/jkugiya/ulid-scala/badge.svg?branch=master)](https://coveralls.io/github/jkugiya/ulid-scala?branch=master)  

ULID (Universally Unique Lexicographically Sortable Identifier) generator and parser for Scala.

Refer [alizain/ulid](https://github.com/alizain/ulid) for a more detailed ULID specification.

## Getting Started

```
libraryDependencies += "com.github.jkugiya" %% "ulid" % "<version>"
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


## LICENSE
[LICENSE](https://github.com/jkugiya/ulid-scala/blob/master/LICENSE)

## Prior Arts
- [azam/ulidj](https://github.com/azam/ulidj)
- [sulky-ulid](https://github.com/huxi/sulky/tree/master/sulky-ulid)
- [ulid4s](https://github.com/petitviolet/ulid4s)

