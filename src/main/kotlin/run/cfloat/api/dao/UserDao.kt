package run.cfloat.api.dao

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import run.cfloat.entity.Users

class UserDao {
  fun getUserByName(name: String) {
     transaction {
      Users.select {
        Users.name eq name
      }.map {

      }.firstOrNull()
    }
  }
}