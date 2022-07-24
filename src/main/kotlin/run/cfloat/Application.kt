package run.cfloat

import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.sqlite.SQLiteConfig
import run.cfloat.common.Config
import run.cfloat.entity.Users
import run.cfloat.plugins.*
import java.sql.Connection

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
  val app = AppCore()
  configureDatabase()
  configureController(app)
  configureSerialization()
  configureAuthorization()
}
/** 初始化数据库 */