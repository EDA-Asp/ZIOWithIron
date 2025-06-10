package ZIOWithIron

import ZIOWithIron.Domain.{User, UserName,Age}
import io.getquill.jdbczio.Quill
import io.getquill.*
import zio.{IO, Task, ZIO, ZLayer}

import java.sql.SQLException
import java.time.ZonedDateTime
import java.util.UUID

trait UserRepo:
  def createUser(user: User): IO[SQLException, Unit]

class UserRepoLive(dbContext: Quill.Postgres[SnakeCase]) extends UserRepo:

  import dbContext.*
  inline given schema: SchemaMeta[UserDb] = schemaMeta[UserDb]("users")

  def createUser(user: User): IO[SQLException, Unit] = {

    val userDb = UserDb.fromUser(user)
    run {
      query[UserDb]
        .insertValue(lift(userDb))
    }.unit
  }

  def allUsers: IO[SQLException, List[User]] = {

    run {
      query[UserDb]
    }
  }.map(x=>x.map(_.toUser()))


case class UserDb(
    userId: UUID,
    name: String,
    age: Int,
    createdAt: ZonedDateTime = ZonedDateTime.now()
):
  def toUser(): User = User(userId, UserName.applyUnsafe(name), Age.applyUnsafe(age))


object UserDb:
  def fromUser(user: User): UserDb = UserDb(user.userId, user.name, user.age)


object UserRepoLive:
  val layer: ZLayer[Quill.Postgres[SnakeCase], Nothing, UserRepoLive] = ZLayer {
    ZIO.serviceWith[Quill.Postgres[SnakeCase]](UserRepoLive(_))
  }
