package run.cfloat

import io.ktor.server.application.*
import io.ktor.server.netty.*
import run.cfloat.plugins.configureController
import run.cfloat.plugins.configureRouting
import run.cfloat.plugins.configureSerialization

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureController()
    configureSerialization()
    configureRouting()
}