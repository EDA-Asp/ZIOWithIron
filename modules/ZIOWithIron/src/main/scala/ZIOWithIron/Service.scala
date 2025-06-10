package ZIOWithIron

import ZIOWithIron.Domain.User
import zio.{IO, ZIO, ZLayer}

import java.sql.SQLException

trait UserService:
  def createUser(user: User): IO[SQLException,Unit]

class UserServiceLive(userRepo: UserRepo) extends UserService {
  override def createUser(user: User): IO[SQLException,Unit] = userRepo.createUser(user)
}

object UserServiceLive:
  val layer: ZLayer[UserRepo, Nothing, UserServiceLive] = ZLayer {
    ZIO.serviceWith[UserRepo](UserServiceLive(_))
  }
