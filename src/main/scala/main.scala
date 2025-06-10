import io.getquill.SnakeCase
import io.getquill.jdbczio.Quill
import zio.ZIOAppDefault
import zio.http.Server
import ZIOWithIron.{Router, UserRepoLive, UserServiceLive}

object app extends ZIOAppDefault:

  val run = Server
    .serve(Router.routes ++ Router.swaggerRoutes)
    .provide(
      Server.default,
      UserRepoLive.layer,
      UserServiceLive.layer,
      Quill.Postgres.fromNamingStrategy(SnakeCase),
      Quill.DataSource.fromPrefix("mydbconf")
    )