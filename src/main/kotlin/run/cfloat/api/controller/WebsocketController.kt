package run.cfloat.api.controller

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import run.cfloat.api.service.WebsocketService
import run.cfloat.model.SendMessageSocket
import run.cfloat.pkg.OnlineUser
import run.cfloat.pkg.AppResponse

val svc = WebsocketService()

/** 用于处理用户发来的消息 */
fun generateOnlineUser(ctx: DefaultWebSocketSession, userID: Int): OnlineUser {
  return OnlineUser(ctx, userID) {
    logout {
      svc.clearOnlineUser(id)
    }
    // 发送消息
    method("sendMessage", SendMessageSocket::class.java) {
      val result = mapOf(
        "message" to it.params.message
      )
      it.toSuccess(result)
    }
  }
}

fun Route.websocketController() {
  authenticate("auth") {
    webSocket("/ws") {
      val resp = AppResponse(this.call).build<Any>()
      // 如果当前用户已经在线就踢下来
      if (svc.checkUserOnline(resp.userID)) {
        svc.offlineUser(resp.userID, "你被挤下来了")
      }
      val onlineUser = generateOnlineUser(this, resp.userID)
      svc.loginUser(onlineUser)
    }
  }
}