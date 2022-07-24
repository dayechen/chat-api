package run.cfloat

import io.ktor.server.application.*
import io.ktor.server.netty.*
import run.cfloat.common.Config
import run.cfloat.plugins.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
  val app = AppCore()
  configureController(app)
  configureSerialization()
  configureAuthorization()
}