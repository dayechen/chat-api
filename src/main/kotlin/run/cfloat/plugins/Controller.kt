package run.cfloat.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import run.cfloat.api.controller.customerController

fun Application.configureController() {
  routing {
    customerController()
  }
}
