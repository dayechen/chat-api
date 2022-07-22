package run.cfloat.plugins

import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {
  authentication {
    jwt {
      val jwtAudience = this@configureRouting.environment.config.property("jwt.audience").getString()
      realm = this@configureRouting.environment.config.property("jwt.realm").getString()
      verifier(
        JWT.require(Algorithm.HMAC256("secret")).withAudience(jwtAudience)
          .withIssuer(this@configureRouting.environment.config.property("jwt.domain").getString()).build()
      )
      validate { credential ->
        if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
      }
    }
  }
}
