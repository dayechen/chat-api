package run.cfloat.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import jakarta.validation.Validation
import jakarta.validation.Validator
import run.cfloat.model.ResultDot


// 同意的相应
class Response<T>(
  val params: T, private val ctx: PipelineContext<Unit, ApplicationCall>
) {
  suspend fun toSuccess() {
    ctx.call.respond(
      message = ResultDot(
        message = "success",
        code = 0,
        data = mapOf("chendaye" to 666)
      )
    )
  }
}


class AppCore {
  val factory: Validator = Validation.buildDefaultValidatorFactory().validator

  suspend inline fun <reified T : Any> bind(ctx: PipelineContext<Unit, ApplicationCall>): Response<T> {
    val params = ctx.call.receive<T>()
    val errors = factory.validate(params)
    if (errors.isNotEmpty()) {
      ctx.call.respondText(errors.iterator().next().message)
    }
    return Response(params, ctx)
  }
}