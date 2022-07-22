package run.cfloat.model

import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.Length

data class LoginRequest(
  @get:Length(min = 2, max = 20, message = "最小2最大20") val username: String,
  @get:Length(min = 2, max = 20, message = "密码不能为空") val password: String
)

