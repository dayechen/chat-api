package run.cfloat.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import run.cfloat.api.controller.friendController
import run.cfloat.api.controller.userController
import run.cfloat.model.ParameterException


fun Application.configureController(app: AppCore) {
  routing {
    try {
      userController(app)
      friendController(app)
    } catch (e: ParameterException) {
      println(e.message)
    }
  }
}
