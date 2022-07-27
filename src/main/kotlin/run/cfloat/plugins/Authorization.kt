package run.cfloat.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import run.cfloat.pkg.Config
import run.cfloat.pkg.Config.audience
import run.cfloat.pkg.Config.issuer
import run.cfloat.pkg.Config.myRealm
import run.cfloat.pkg.Config.secret
import run.cfloat.model.ResultDTO

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
          HttpStatusCode.Unauthorized, ResultDTO(
            message = "登录失败",
            code = 401
          )
        )
      }
    }
  }
}
