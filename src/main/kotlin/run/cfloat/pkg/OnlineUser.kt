package run.cfloat.pkg

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.ktor.websocket.*
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.json.simple.JSONObject
import run.cfloat.model.ParameterException
import run.cfloat.model.ResultDTO
import kotlin.math.log


class OnlineUser(
  private val ctx: DefaultWebSocketSession, val id: Int, handler: OnlineUser.() -> Unit
) {

  /** 用于处理用户发来消息的方法 */
  private val methodMap = mutableMapOf<String, suspend (JsonObject, Int?) -> Unit>()

  /** 用户消息 */
  data class MessageDTO(
    /** 调用的方法名 */
    val method: String,
    /** 消息id，直接传回前端 */
    val messageID: Int? = null,
    /** 发送的数据 */
    val params: JsonObject
  )

  /** 返回的消息 */
  data class SocketResultDTO(
    val message: String,
    val code: Int,
    val data: Any = JSONObject(),
    val messageID: Int? = null
  )


  /** 用户退出登录 */
  private var logoutHandle: () -> Unit = {}

  init {
    handler()
  }

  /** 接收用户消息 */
  private suspend fun receiveMessage(frame: Frame) {
    if (frame is Frame.Text) {
      val text = frame.readText()
      val data = gson.fromJson(text, MessageDTO::class.java)
      val method = methodMap[data.method]
      if (method != null) {
        method(data.params, data.messageID)
      } else {
        // 没有定义方法
        toError("未找到方法", 1, data.messageID)
      }
      // 解析json字符串
      if (text.equals("bye", ignoreCase = true)) {
        ctx.close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
      }
    }
  }

  /** 与用户建立连接 */
  suspend fun connect() {
    for (frame in ctx.incoming) {
      receiveMessage(frame)
    }
    // 退出登录
    logoutHandle()
  }

  /** 给用户发送消息 */
  private suspend fun send(data: Any) {
    ctx.send(Frame.Text(gson.toJson(data)))
  }

  /** 发送错误的消息 */
  private suspend fun toError(message: String, code: Int = 1, messageID: Int? = null) {
    send(
      SocketResultDTO(
        message = message, code = code, messageID = messageID
      )
    )
  }

  /** 发送成功的消息 */
  suspend fun toSuccess(data: Any = JSONObject(), messageID: Int? = null) {
    send(
      SocketResultDTO(
        message = "success", code = 0, messageID = messageID, data = data
      )
    )
  }

  /** 下线该用户 */
  suspend fun offline(message: String) {
    ctx.close(CloseReason(CloseReason.Codes.NORMAL, message))
  }

  /** 用于回复的工具,主要就是为了加上messageID */
  class EchoHandle<T>(val params: T, private val messageID: Int?, private val ctx: OnlineUser) {
    /** 返回正确的数据 */
    suspend fun toSuccess(data: Any = JSONObject()) {
      ctx.toSuccess(data, messageID)
    }

    /** 返回错误的信息 */
    suspend fun toError(message: String, code: Int = 1) {
      ctx.toError(message, code, messageID)
    }
  }

  /** 接受用户发来的消息 */
  fun <T : Any?> method(url: String, type: Class<T>, handler: suspend (EchoHandle<T>) -> Unit) {
    fun generateMethod(): suspend (JsonObject, Int?) -> Unit {
      return { data, messageID ->
        // 解析json
        val params = gson.fromJson(data, type)
        // 回复用的工具
        val echoHandle = EchoHandle(params, messageID, this)
        // 验证参数
        val errors = validatorFactory.validate(params)
        if (errors.isNotEmpty()) {
          val errMessage = errors.iterator().next().message
          echoHandle.toError(errMessage)
        } else {
          handler(echoHandle)
        }
      }
    }
    methodMap[url] = generateMethod()
  }

  /** 用户退出登录 */
  fun logout(handler: () -> Unit) {
    logoutHandle = handler
  }
}
