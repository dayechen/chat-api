package run.cfloat.plugins

import io.ktor.serialization.gson.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import run.cfloat.api.controller.websocketController
import run.cfloat.model.ParameterException


fun Application.configureSerialization() {
  install(WebSockets) {
    pingPeriod = Duration.ofSeconds(15)
    timeout = Duration.ofSeconds(15)
    maxFrameSize = Long.MAX_VALUE
    masking = false
  }
  install(ContentNegotiation) {
    gson()
  }
  routing {
    try {
      websocketController()
    } catch (e: ParameterException) {
      println(e.message)
    }
  }
}
