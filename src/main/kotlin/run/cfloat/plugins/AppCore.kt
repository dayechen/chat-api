package run.cfloat.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.json.simple.JSONObject
import run.cfloat.entity.Users
import run.cfloat.model.ParameterException
import run.cfloat.model.ResultDot
import java.sql.Connection


// 同意的相应
class Response<T>(
  val params: T, private val ctx: PipelineContext<Unit, ApplicationCall>
) {
  suspend fun toSuccess(data: Any = JSONObject()) {
    ctx.call.respond(
      message = ResultDot(
        message = "success",
        code = 0,
        data = data
      )
    )
  }

  suspend fun toError(message: String, code: Int = 1) {
    ctx.call.respond(
      message = ResultDot(
        message = message,
        code = code,
      )
    )
  }
}


class AppCore {
  val factory: Validator = Validation.buildDefaultValidatorFactory().validator
  init {
    setupDataBase()
  }
  private fun setupDataBase(){
    val path = "C:\\Home\\Code\\kotlin\\chat-api\\sql.db"
    Database.connect("jdbc:sqlite:$path", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel =
      Connection.TRANSACTION_SERIALIZABLE
    transaction {
      SchemaUtils.create(Users)
    }
  }
  suspend inline fun <reified T : Any> bind(ctx: PipelineContext<Unit, ApplicationCall>): Response<T> {
    val params = ctx.call.receive<T>()
    val errors = factory.validate(params)
    val response = Response(params, ctx)
    if (errors.isNotEmpty()) {
      val errMessage = errors.iterator().next().message
      response.toError(errMessage)
      throw ParameterException(errMessage)
    }
    return response
  }
}