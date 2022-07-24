package run.cfloat.api.service

import run.cfloat.api.dao.FriendDao
import run.cfloat.entity.Friends

class FriendService {
  private val dao = FriendDao()

  /**
   * 获取好友申请状态
   * @return 0:没有申请状态 1:被拒绝了 2:还未回复
   * */
  fun getApplyStatus(userID: Int, friendID: Int): Pair<Int, String> {
    val row = dao.getApplyFriendCount(userID, friendID) ?: return Pair(0, "")
    val refuseMessage = row[Friends.refuseMessage]
    if (refuseMessage != null) {
      return Pair(1, refuseMessage)
    }
    if (!row[Friends.processed]) {
      return Pair(2, "已经发出申请")
    }
    return Pair(3, "")
  }

  /** 检查好友是否存在 */
  fun checkFriend(friendID: Int): Boolean {
    return dao.getUserCount(friendID) > 0
  }

  /**
   * 发送好友申请
   * */
  fun sendApplyMessage(userID: Int, friendID: Int, verifyMessage: String) {
    dao.insertApply(userID, friendID, verifyMessage)
  }
}