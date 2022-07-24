package run.cfloat.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.Length

data class LoginRequest(
  @get:NotNull(message = "不能为空") @get:Length(min = 2, max = 20, message = "最小2最大20") val username: String,
  @get:NotNull(message = "不能为空") @get:Length(min = 2, max = 20, message = "密码不能为空") val password: String
)

data class AddFriendRequest(
  @get:NotNull(message = "请选择好友")
  val friendID: Int,
  @get:NotNull(message = "请输入申请消息")
  val applyMessage: String
)
