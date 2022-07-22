package run.cfloat.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.configureSerialization() {
  install(WebSockets) {
    pingPeriod = Duration.ofSeconds(15)
    timeout = Duration.ofSeconds(15)
    maxFrameSize = Long.MAX_VALUE
    masking = false
  }
  install(ContentNegotiation) {
    json()
  }

  routing {
    webSocket("/ws") { // websocketSession
      for (frame in incoming) {
        if (frame is Frame.Text) {
          val text = frame.readText()
          outgoing.send(Frame.Text("YOU SAID: $text"))
          if (text.equals("bye", ignoreCase = true)) {
            close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
          }
        }
      }
    }
  }
}
