package run.cfloat.api.service

import org.jetbrains.exposed.sql.ResultRow
import run.cfloat.api.dao.FriendDao
import run.cfloat.entity.Friend
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
    return Pair(2, "已经发出申请")
  }

  /** 获取好友的申请信息 */
  fun getApplyList(userID: Int): List<Friend> {
    return dao.getApplyList(userID)
  }

  /** 获取好友列表 */
  fun getFriendList(userID: Int): List<Friend> {
    return dao.getFriendList(userID)
  }

  /** 检查好友是否存在 */
  fun checkFriend(friendID: Int): Boolean {
    return dao.getUserCount(friendID) > 0
  }

  /** 发送好友申请 */
  fun sendApplyMessage(userID: Int, friendID: Int, verifyMessage: String) {
    dao.insertApply(userID, friendID, verifyMessage)
  }

  /** 通过好友申请 */
  fun passAudi(applyID: Int): Boolean {
    return dao.activeFriend(applyID) > 0
  }

  /** 拒绝好友申请 */
  fun refuseAudi(applyID: Int, refuseMessage: String?): Boolean {
    return dao.refuseAudi(applyID, refuseMessage) > 0
  }
}