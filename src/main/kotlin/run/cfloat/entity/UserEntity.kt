package run.cfloat.entity

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.CurrentDateTime
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object Users : Table() {
  val id = integer("id").autoIncrement()

  /** 用户名 */
  val name = varchar("name", 50)

  /** 密码 */
  val password = varchar("password", 100)
  override val primaryKey = PrimaryKey(id)
}

