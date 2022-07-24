package run.cfloat.api.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.jetbrains.exposed.sql.ResultRow
import run.cfloat.api.dao.UserDao
import run.cfloat.common.Config.audience
import run.cfloat.common.Config.issuer
import run.cfloat.common.Config.secret
import run.cfloat.entity.Users
import run.cfloat.java.MD5Utils
import run.cfloat.model.LoginRequest
import java.util.*

class UserService {
  private val dao = UserDao()
  fun getUserByName(name: String): ResultRow? {
    return dao.getUser(name)
  }

  fun getUserID(name: String, password: String): Int? {
    return dao.getUserID(name, password)
  }

  fun generateToken(userID: Int): String {
    return JWT.create().withAudience(audience).withIssuer(issuer).withClaim("userID", userID)
      .withExpiresAt(Date(System.currentTimeMillis() + 6666666666)).sign(Algorithm.HMAC256(secret))
  }

  fun createUser(params: LoginRequest) {
    dao.creteUser(params.username, params.password)
  }
}