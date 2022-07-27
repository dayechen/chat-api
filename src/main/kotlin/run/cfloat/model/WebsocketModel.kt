package run.cfloat.model

import jakarta.validation.constraints.NotNull

/**  发送消息 */
data class SendMessageSocket(
  /** 发送给这个用户 */
  @get:NotNull(message = "好友ID不能为空") val targetUserID: Int,
  /** 消息 */
  @get:NotNull(message = "消息不能为空") val message: String
)