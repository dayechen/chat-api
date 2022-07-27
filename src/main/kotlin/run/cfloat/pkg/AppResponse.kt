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
import org.json.simple.JSONObject
import run.cfloat.model.ParameterException
import run.cfloat.model.ResultDTO


/** 用于处理响应 */
class Response<T>(
  val params: T, private val call: ApplicationCall, var userID: Int = 0
) {
  suspend fun toSuccess(data: Any = JSONObject()) {
    call.respond(
      status = HttpStatusCode.OK, message = ResultDTO(
        message = "success", code = 0, data = data
      )
    )
  }

  suspend fun toError(message: String, code: Int = 1, status: HttpStatusCode = HttpStatusCode.BadRequest) {
    call.respond(
      status = status, message = ResultDTO(
        message = message,
        code = code,
      )
    )
  }
}

class AppResponse(
  val call: ApplicationCall, val verify: Boolean = true
) {
  val factory: Validator = Validation.buildDefaultValidatorFactory().validator

  constructor(
    ctx: PipelineContext<Unit, ApplicationCall>,
    verify: Boolean = true,
  ) : this(ctx.call, verify)

  suspend inline fun <reified T : Any> build(
  ): Response<T> {
    val params = call.receive<T>()
    val errors = factory.validate(params)
    val response = Response(params, call)
    if (verify) {
      response.userID = call.principal<JWTPrincipal>()!!.payload.getClaim("userID").asInt()
    }
    if (errors.isNotEmpty()) {
      val errMessage = errors.iterator().next().message
      response.toError(errMessage)
      throw ParameterException(errMessage)
    }
    return response
  }
}