package run.cfloat.plugins

import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import run.cfloat.common.Config
import run.cfloat.common.Config.audience
import run.cfloat.common.Config.domain
import run.cfloat.common.Config.issuer
import run.cfloat.common.Config.myRealm
import run.cfloat.common.Config.secret
import run.cfloat.model.ResultDot

fun Application.configureAuthorization() {
  Config.init(this@configureAuthorization.environment.config)
  authentication {
    jwt("auth") {
      val jwtAudience = audience
      realm = myRealm
      verifier(
        JWT.require(Algorithm.HMAC256(secret))
          .withAudience(jwtAudience)
          .withIssuer(issuer)
          .build()
      )
      validate { credential ->
        JWTPrincipal(credential.payload)
      }
      challenge { _, _ ->
        call.respond(
          HttpStatusCode.Unauthorized, ResultDot(
            message = "登录失败",
            code = 401
          )
        )
      }
    }
  }
}
