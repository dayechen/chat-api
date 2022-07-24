package run.cfloat.api.dao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import run.cfloat.entity.Friends
import run.cfloat.entity.Users

class FriendDao {
  /** 获取申请好友的数量 */
  fun getApplyFriendCount(userID: Int, friendID: Int): ResultRow? {
    return transaction {
      Friends.select {
        (Friends.userID eq userID) and (Friends.friendID eq friendID) and (Friends.active eq false)
      }.firstOrNull()
    }
  }

  /** 发送好友申请 */
  fun insertApply(userID: Int, friendID: Int, verifyMessage: String) {
    transaction {
      Friends.insert {
        it[Friends.userID] = userID
        it[Friends.friendID] = friendID
        it[Friends.verifyMessage] = verifyMessage
      }
    }
  }

  /** 判断用户是否存在 */
  fun getUserCount(userID: Int): Long {
    return transaction {
      Users.select {
        Users.id eq userID
      }.count()
    }
  }
}