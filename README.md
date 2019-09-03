# ULID scala
[![Build Status](https://travis-ci.org/jkugiya/ulid-scala.png?branch=master)](https://travis-ci.org/jkugiya/ulid-scala)
[![Coverage Status](https://coveralls.io/repos/github/jkugiya/ulid-scala/badge.svg?branch=master)](https://coveralls.io/github/jkugiya/ulid-scala?branch=master)  

ULID (Universally Unique Lexicographically Sortable Identifier) generator and parser for Scala.

Refer [alizain/ulid](https://github.com/alizain/ulid) for a more detailed ULID specification.

## Getting Started

```
libraryDependencies += "jkugiya" %% "ulid" % "<version>"
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
