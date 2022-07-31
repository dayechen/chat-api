package run.cfloat.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.sqlite.SQLiteConfig
import run.cfloat.entity.Friends
import run.cfloat.entity.Users
import java.io.File
import java.sql.Connection

fun configureDatabase() {
  val path = "D:\\code\\kotlin\\sex-chat-room\\sql.db"
  val dbFile = File(path)
  if (!dbFile.exists()) {
    dbFile.createNewFile()
  }
  Database.connect(url = "jdbc:sqlite:$path", driver = "org.sqlite.JDBC", setupConnection = {
    SQLiteConfig().apply {
      // Some options that could help with this but don't
      setSharedCache(true)
      setJournalMode(SQLiteConfig.JournalMode.WAL)
      setLockingMode(SQLiteConfig.LockingMode.EXCLUSIVE)
      apply(it)
    }
  })
  TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
  transaction {
    addLogger()
    SchemaUtils.createMissingTablesAndColumns(Users, Friends)
  }
}