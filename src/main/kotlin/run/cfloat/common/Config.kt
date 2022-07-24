package run.cfloat.common

import io.ktor.server.config.*

object Config {
  var secret: String = ""
  var issuer: String = ""
  var audience: String = ""
  var myRealm: String = ""
  var domain :String  =""
  fun init(config: ApplicationConfig) {
    secret = config.property("jwt.secret").getString()
//    issuer = config.property("jwt.issuer").getString()
    audience = config.property("jwt.audience").getString()
    myRealm = config.property("jwt.realm").getString()
    domain = config.property("jwt.domain").getString()
  }
}