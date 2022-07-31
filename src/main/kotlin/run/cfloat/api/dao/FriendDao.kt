package run.cfloat.api.dao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import run.cfloat.entity.Friend
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

  /** 获取全部申请信息 */
  fun getApplyList(userID: Int): List<Friend> {
    return transaction {
      Friends.select {
        (Friends.friendID eq userID) and (Friends.active eq false)
      }.map { Friends.format(it) }
    }
  }

  /** 获取好友列表 */
  fun getFriendList(userID: Int): List<Friend> {
    return transaction {
      Friends.select {
        (Friends.userID eq userID) and (Friends.active eq true)
      }.map {
        Friends.format(it)
      }
    }
  }

  /** 激活好友 */
  fun activeFriend(applyID: Int): Int {
    return transaction {
      Friends.update({
        (Friends.id eq applyID) and (Friends.processed eq false)
      }) {
        it[active] = true
        it[processed] = true
        // 获取数据
        val applyRow = Friends.slice(friendID, userID, verifyMessage).select {
          Friends.id eq applyID
        }.first()
        // 把好友id和用户id反过来
        Friends.insert { it1 ->
          it1[userID] = applyRow[friendID]
          it1[friendID] = applyRow[userID]
          it1[verifyMessage] = applyRow[verifyMessage]
          it1[active] = true
          it[processed] = true
        }
      }
    }
  }

  fun refuseAudi(applyID: Int, refuseMessage: String?): Int {
    return transaction {
      Friends.update({
        (Friends.id eq applyID) and (Friends.processed eq false)
      }) {
        it[processed] = true
        it[Friends.refuseMessage] = refuseMessage
      }
    }
  }
}