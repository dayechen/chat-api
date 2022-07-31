package run.cfloat.pkg

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

/** 基础的用户响应 */
class AppResponse(
  /** 用于直接返回消息 */
  val call: ApplicationCall,
  /** 是否进行用户认证 */
  val verify: Boolean = true
) {

  /** 处理响应的工具 */
  class ResponseHandle<T>(
    val params: T, private val call: ApplicationCall, var userID: Int = 0
  ) {
    /** 返回成功消息 */
    suspend fun toSuccess(data: Any = JSONObject()) {
      call.respond(
        status = HttpStatusCode.OK, message = ResultDTO(
          message = "success", code = 0, data = data
        )
      )
    }

    /** 返回失败消息 */
    suspend fun toError(message: String, code: Int = 1, status: HttpStatusCode = HttpStatusCode.BadRequest) {
      call.respond(
        status = status, message = ResultDTO(
          message = message,
          code = code,
        )
      )
    }
  }

  constructor(
    ctx: PipelineContext<Unit, ApplicationCall>,
    verify: Boolean = true,
  ) : this(ctx.call, verify)

  /** 构建请求的工具 */
  suspend inline fun <reified T : Any> build(
  ): ResponseHandle<T> {
    // 格式化参数
    val params = call.receive<T>()
    // 设置处理的方法
    val response = ResponseHandle(params, call)
    if (verify) {
      // 如果需要登录就验证后获取用于id
      response.userID = call.principal<JWTPrincipal>()!!.payload.getClaim("userID").asInt()
    }
    // 验证参数
    val errors = validatorFactory.validate(params)
    if (errors.isNotEmpty()) {
      val errMessage = errors.iterator().next().message
      response.toError(errMessage)
      throw ParameterException(errMessage)
    }
    return response
  }
}