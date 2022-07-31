package run.cfloat.api.controller

import io.ktor.server.routing.*
import run.cfloat.api.service.UserService
import run.cfloat.model.LoginRequest
import run.cfloat.pkg.AppResponse

fun Route.userController() {
  val svc = UserService()
  route("/verification") {
    put {
      val resp = AppResponse(this, false).build<LoginRequest>()
      if (svc.getUserByName(resp.params.username) != null) {
        return@put resp.toError("当前用户已经存在")
      }
      svc.createUser(resp.params)
      resp.toSuccess("创建成功")
    }
    post {
//      val resp = app.bind<LoginRequest>(this, false)
      val resp = AppResponse(this, false).build<LoginRequest>()
      val userID = svc.getUserID(resp.params.username, resp.params.password) ?: return@post resp.toError("用户名或密码错误")
      val token = svc.generateToken(userID)
      resp.toSuccess(mapOf("token" to token))
    }
  }
}