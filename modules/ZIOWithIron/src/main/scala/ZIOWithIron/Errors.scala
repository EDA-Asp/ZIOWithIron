package ZIOWithIron

import zio.schema.*

sealed trait AppError derives Schema
object AppError:
  case class PersistentError(info: String) extends AppError derives Schema
