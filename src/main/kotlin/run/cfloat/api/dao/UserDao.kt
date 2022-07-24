package run.cfloat.api.dao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import run.cfloat.entity.Users
import run.cfloat.model.LoginRequest

class UserDao {
  fun getUser(name: String): ResultRow? {
    return transaction {
      Users.select {
        Users.name eq name
      }.firstOrNull()
    }
  }

  fun getUserID(name: String, password: String): Int? {
    val query = transaction {
      Users.slice(Users.id).select {
        (Users.name eq name) and (Users.password eq password)
      }.firstOrNull()
    } ?: return null
    return query[Users.id]
  }

  fun creteUser(name: String, password: String) {
    transaction {
      Users.insert {
        it[Users.name] = name
        it[Users.password] = password
      }
    }
  }
}