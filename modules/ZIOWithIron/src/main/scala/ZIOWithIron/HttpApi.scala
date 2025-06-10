package ZIOWithIron

import ZIOWithIron.AppError.PersistentError
import ZIOWithIron.Domain.*
import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio.http.*
import zio.http.endpoint.Endpoint
import zio.http.codec.*
import utils.given
import zio.http.endpoint.AuthType.None
import zio.http.endpoint.openapi.{OpenAPIGen, SwaggerUI}
import zio.{Random, ZIO, ZIOAppDefault}
import zio.schema.*

import java.util.UUID

object Endpoints:

  import io.github.iltotore.iron.*
  import zio.http.*
  import zio.http.codec.*

  case class createUserCommand(name: UserName, age: Age) derives Schema

  val createUser: Endpoint[Unit, createUserCommand, PersistentError, UserId, None] =
    Endpoint(Method.POST / "users")
      .in[createUserCommand](Doc.p("user to create"))
      .out[UserId](Doc.p("id of created user"))
      .outError[PersistentError](Status.InternalServerError, Doc.p("some error"))
      ?? Doc.p("Add an item to a user's cart")

object Handlers:

  import ZIOWithIron.Endpoints.createUserCommand

  def handleCreateUserCommand(
      command: createUserCommand
  ): ZIO[UserService, PersistentError, UserId] =

    for
      userId <- Random.nextUUID
      user = User(userId, command.name, command.age)
      _ <- ZIO
        .serviceWithZIO[UserService](_.createUser(user))
        .mapError(e => PersistentError(e.toString))
    yield user.userId

object Router:
  import Endpoints.*
  import Handlers.*

  val routes: Routes[UserService, Nothing] = Routes(
    createUser.implementHandler(handler(handleCreateUserCommand))
  )
  val swaggerRoutes: Routes[Any, Response] =
    SwaggerUI.routes("docs", OpenAPIGen.fromEndpoints(createUser))
