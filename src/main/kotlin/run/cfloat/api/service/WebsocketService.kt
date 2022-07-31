package run.cfloat.api.service

import run.cfloat.pkg.OnlineUser

class WebsocketService {
  /** 存储所有的在线用户 */
  private val onlineUserMap = mutableMapOf<Int, OnlineUser>()

  /** 检查用户是否在线 */
  fun checkUserOnline(userID: Int): Boolean {
    return onlineUserMap[userID] != null
  }

  /** 强制下线 */
  suspend fun offlineUser(userID: Int, message: String) {
    onlineUserMap[userID]?.offline(message)
  }

  /** 清除用户信息 */
  fun clearOnlineUser(userID: Int) {
    onlineUserMap.remove(userID)
  }

  /** 登录用户 */
  suspend fun loginUser(onlineUser: OnlineUser) {
    onlineUserMap[onlineUser.id] = onlineUser
    onlineUser.connect()
  }
}