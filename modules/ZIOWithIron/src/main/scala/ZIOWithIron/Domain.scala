package ZIOWithIron

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import zio.schema.*
import utils.given
import java.util.UUID

object Domain:

  type UserId = UUID

  type AgeDescription = DescribedAs[Greater[0], "User's age should be strictly positive"]

  type Age = Int :| AgeDescription

  object Age extends RefinedType[Int, AgeDescription]

  type UserNameDescription =
    DescribedAs[
      Alphanumeric & Not[Empty] & MaxLength[100],
      "User's name should be alphanumeric, non-empty and have a maximum length of 100"
    ]

  type UserName = String :| UserNameDescription

  object UserName extends RefinedType[String, UserNameDescription]

  case class User(userId: UserId, name: UserName, age: Age) derives Schema
