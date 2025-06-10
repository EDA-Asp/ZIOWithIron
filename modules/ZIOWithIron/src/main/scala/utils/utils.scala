package utils

import io.github.iltotore.iron.*
import zio.schema.*

inline given ironSchema[T, Description](using
    Schema[T],
    Constraint[T, Description]
): Schema[T :| Description] =
  Schema[T].transformOrFail(_.refineEither[Description], Right(_))

