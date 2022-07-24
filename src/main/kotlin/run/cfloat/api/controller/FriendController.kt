package run.cfloat.api.controller

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import run.cfloat.api.service.FriendService
import run.cfloat.model.AddFriendRequest
import run.cfloat.plugins.AppCore

fun Route.friendController(app: AppCore) {
  val svc = FriendService()
  authenticate("auth") {
    route("/friend") {
      /** 获取好友列表 */
      get("/list") {
        val resp = app.bind<Any>(this)
        resp.toSuccess("这里是好友列表")
      }
      /** 添加好友 */
      post {
        val resp = app.bind<AddFriendRequest>(this)
        /** 查找好友是否存在 */
        if (!svc.checkFriend(resp.params.friendID)) {
          return@post resp.toError("找不到好友")
        }
        /** 查找是否已经申请过了 */
        val (friendStatus, message) = svc.getApplyStatus(resp.userID, resp.params.friendID)
        if (friendStatus == 1 || friendStatus == 2) {
          return@post resp.toError(message)
        }
        /** 发送好友申请 */
        svc.sendApplyMessage(resp.userID, resp.params.friendID, resp.params.applyMessage)
        resp.toSuccess("申请成功")
      }
    }
  }
}
