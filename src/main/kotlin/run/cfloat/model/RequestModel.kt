package run.cfloat.model

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.Length
import org.json.simple.JSONObject

/** 返回的消息 */
data class ResultDTO(
  val message: String,
  val code: Int,
  val data: Any = JSONObject(),
)

/** 登录 */
data class LoginRequest(
  /** 用户名 */
  @field:NotNull(message = "不能为空") @field:Length(min = 2, max = 20, message = "最小2最大20") val username: String,
  /** 密码 */
  @field:NotNull(message = "不能为空") @field:Length(min = 2, max = 20, message = "密码不能为空") val password: String
)

/** 添加好友 */
data class AddFriendRequest(
  /** 被添加的好友id */
  @field:NotNull(message = "请选择好友") val friendID: Int,
  /** 申请好友的验证消息 */
  @field:NotNull(message = "请输入申请消息") val applyMessage: String
)

/** 通过验证 */
data class PutApplyRequest(
  @get:NotNull(message = "请选择消息")
  /** 申请消息的id */
  val applyID: Int,
  /** 消息状态 0:拒绝添加 1:通过审核 */
  val status: Int,
  /**  拒绝理由 */
  val refuseMessage: String?
)

