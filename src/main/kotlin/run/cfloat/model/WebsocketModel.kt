package run.cfloat.model

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

/**  发送消息 */
data class SendMessageSocket(
  /** 接受消息的用户id */
  @field:Min(value = 0, message = "好友ID不能为空") val targetUserID: Int = -1,
  /** 消息 */
  @field:NotNull(message = "消息不能为空") val message: String = ""
)