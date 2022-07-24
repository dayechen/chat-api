package run.cfloat.api.controller

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import run.cfloat.api.service.UserService
import run.cfloat.model.LoginRequest
import run.cfloat.plugins.AppCore

fun Route.userController(app: AppCore) {
  val service = UserService()
  route("/customer") {
    get {
      call.respondText("666")
    }
  }
  route("/verification") {
    put {
      val resp = app.bind<LoginRequest>(this)
      resp.toSuccess(resp.params)
    }
  }
}