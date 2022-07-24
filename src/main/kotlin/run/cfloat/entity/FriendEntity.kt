package run.cfloat.entity

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object Friends : IntIdTable() {
  /** 用户id */
  val userID = integer("userID")

  /** 添加的好友id */
  val friendID = integer("friendID")

  /** 添加时间 */
  val createTime = datetime("createTime").clientDefault { DateTime.now() }

  /** 这条消息是否被处理过 */
  val processed = bool("processed").default(false)

  /** 验证消息 */
  val verifyMessage = varchar("verifyMessage", 200)

  /** 被拒绝的理由 */
  val refuseMessage = varchar("refuseMessage", 200).nullable()

  /** 是否通过验证 */
  val active = bool("active").default(false)
}