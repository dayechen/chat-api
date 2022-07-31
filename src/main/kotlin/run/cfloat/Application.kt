package run.cfloat

import io.ktor.server.application.*
import io.ktor.server.netty.*
import run.cfloat.plugins.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
  configureDatabase()
  configureController()
  configureSerialization()
  configureAuthorization()
}
/** 初始化数据库 */