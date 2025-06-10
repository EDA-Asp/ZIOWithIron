package repos

import ZIOWithIron.Domain.*
import ZIOWithIron.{Router, UserRepo, UserRepoLive, UserServiceLive}
import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import repos.UserRouteSpec.{suite, test}
import zio.http.*
import zio.http.netty.NettyConfig
import zio.http.netty.server.NettyDriver
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.test.{Spec, TestRandom, ZIOSpecDefault, assertTrue}
import zio.{Random, Scope, ZIO, ZLayer}

object UserRouteSpec extends ZIOSpecDefault with RepositorySpec:

  override val initScript: String = "sql/integration.sql"

  override def spec: Spec[Any, Any] = suite("UserRoutes")(
    test("create and get user by id") {
      for {
        client <- ZIO.service[Client]
        _ <- TestServer.addRoutes(Router.routes)
        port <- ZIO.serviceWithZIO[Server](s => s.port)
        url = URL.root.port(port)
        _ <- TestRandom.setSeed(27)
        rndUid <- Random.nextUUID
        _ <- TestRandom.setSeed(27)
        testUser = User(rndUid, UserName("Adam"), Age(28))
        createResponse <- client(
          Request.post(url / "users", Body.from[User](testUser))
        )
        userId <- createResponse.body.asString(Charsets.Utf8)
      } yield assertTrue(userId.stripPrefix("\"").stripSuffix("\"") == rndUid.toString)
    }
  ).provide(
    Client.default,
    ZLayer.succeed(Server.Config.default.onAnyOpenPort),
    NettyDriver.customized,
    ZLayer.succeed(NettyConfig.defaultWithFastShutdown),
    TestServer.layer,
    Scope.default,
    UserRepoLive.layer,
    Quill.Postgres.fromNamingStrategy(SnakeCase),
    dataSourceLayer,
    UserServiceLive.layer
  )
