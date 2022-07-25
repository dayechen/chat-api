package run.cfloat.model

import org.json.simple.JSONObject

data class ResultDot(
  val message: String,
  val code: Int,
  val data: Any = JSONObject()
)
