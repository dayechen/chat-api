package run.cfloat.entity

import org.jetbrains.exposed.sql.Table

object Users : Table("users") {
  val id = integer("id").autoIncrement()
  val name = varchar("name",50)
  val password = varchar("password",100)
  override val primaryKey = PrimaryKey(id)
}
