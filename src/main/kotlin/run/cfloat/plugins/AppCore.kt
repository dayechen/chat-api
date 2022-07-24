package run.cfloat.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.json.simple.JSONObject
import org.sqlite.SQLiteConfig
import run.cfloat.entity.Users
import run.cfloat.model.ParameterException
import run.cfloat.model.ResultDot
import java.sql.Connection


// 同意的相应
class Response<T>(
  val params: T, private val ctx: PipelineContext<Unit, ApplicationCall>, var userID: Int = 0
) {
  suspend fun toSuccess(data: Any = JSONObject()) {
    ctx.call.respond(
      status = HttpStatusCode.OK, message = ResultDot(
        message = "success", code = 0, data = data
      )
    )
  }

  suspend fun toError(message: String, code: Int = 1, status: HttpStatusCode = HttpStatusCode.BadRequest) {
    ctx.call.respond(
      status = status, message = ResultDot(
        message = message,
        code = code,
      )
    )
  }
}

class AppCore {
  /** 表单验证工具 */
  val factory: Validator = Validation.buildDefaultValidatorFactory().validator

  /** 绑定响应 */
  suspend inline fun <reified T : Any> bind(
    ctx: PipelineContext<Unit, ApplicationCall>,
    verify: Boolean = true,
  ): Response<T> {
    val params = ctx.call.receive<T>()
    val errors = factory.validate(params)
    val response = Response(params, ctx)
    if (verify) {
      response.userID = ctx.call.principal<JWTPrincipal>()!!.payload.getClaim("userID").asInt()
    }
    if (errors.isNotEmpty()) {
      val errMessage = errors.iterator().next().message
      response.toError(errMessage)
      throw ParameterException(errMessage)
    }
    return response
  }
}