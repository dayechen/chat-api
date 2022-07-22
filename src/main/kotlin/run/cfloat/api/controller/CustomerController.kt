package run.cfloat.api.controller

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jakarta.validation.Validation
import run.cfloat.api.model.LoginRequest

fun Route.customerController() {
  route("/customer") {
    get {
      call.respondText("666")
    }
  }
  route("/verification") {
    put {
      val params = call.receive<LoginRequest>()
      val factory = Validation.buildDefaultValidatorFactory().validator
      val errors = factory.validate(params)
      if (errors.isNotEmpty()) {
        call.respondText(errors.iterator().next().message)
        return@put
      }
      call.respondText("没有失败${params.username}")
    }
  }
}