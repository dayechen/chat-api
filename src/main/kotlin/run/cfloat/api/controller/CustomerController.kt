package run.cfloat.api.controller

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jakarta.validation.Validation
import run.cfloat.model.LoginRequest
import run.cfloat.plugins.AppCore

fun Route.customerController(app: AppCore) {
  route("/customer") {
    get {
      call.respondText("666")
    }
  }
  route("/verification") {
    put {
      val resp = app.bind<LoginRequest>(this)
      resp.toSuccess()
    }
  }
}