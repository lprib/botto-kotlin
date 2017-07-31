package win.jaxforreal.botto.frontend

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.simple.JSONValue
import org.json.simple.parser.JSONParser
import win.jaxforreal.botto.Config
import win.jaxforreal.botto.Event
import win.jaxforreal.botto.Log
import java.lang.Exception
import java.net.URI
import javax.net.ssl.SSLContext

open class HackChatFrontend(val username: String, val pass: String, val channel: String) : Frontend {
    override val name = "hackchat"

    override val onMessage = Event<MessageEventArgs>()
    override val onUserJoin = Event<User>()
    override val onUserLeave = Event<User>()

    protected val parser = JSONParser()

    protected open val joinInfoMap get() = mapOf("cmd" to "join", "nick" to "$username#$pass", "channel" to channel)
    protected open val joinURI get() = URI(Config["hackchat", "url"])

    protected open lateinit var ws: WebSocketClient

    override fun sendMessage(message: String) {
        if (ws.isOpen) {
            val messageJson = mapOf("cmd" to "chat", "text" to message).json()
            ws.send(messageJson)
        } else {
            Log.error("message not sent because websocket is closed `$message`")
        }
    }

    override fun connect(): HackChatFrontend {
        ws = getWebSocket()
        doSSLSetup(ws)
        ws.connect()
        return this
    }

    override fun disconnect() {
    }

    open fun doSSLSetup(ws: WebSocketClient) {
        val context = SSLContext.getInstance("TLS")
        context.init(null, null, null) //null, null, null is the defaultProps ssl context
        ws.socket = context.socketFactory.createSocket()
    }

    open fun getWebSocket(): WebSocketClient = object : WebSocketClient(joinURI) {
        override fun onOpen(handshake: ServerHandshake) {
            val text = joinInfoMap.json()
            send(text)
            println("open $name")
        }

        override fun onClose(code: Int, reason: String, remote: Boolean) {
            Log.warn("websocket closed; errorcode:$code reason:\"$reason\" isRemote:$remote")
        }

        @Suppress("UNCHECKED_CAST")
        override fun onMessage(message: String) {
            println(message)
            val data = parser.parse(message) as Map<String, Any>
            if (data["cmd"] == "chat") {
                val user = User(data["nick"] as String, data["trip"] as String? ?: "", this@HackChatFrontend)
                this@HackChatFrontend.onMessage(
                        win.jaxforreal.botto.frontend.MessageEventArgs(
                                user, data["text"] as String, this@HackChatFrontend
                        )
                )
            } else if (data["cmd"] == "onlineAdd") {
                onUserJoin(User(data["nick"] as String, "", this@HackChatFrontend))
            } else if (data["cmd"] == "onlineRemove") {
                onUserLeave(User(data["nick"] as String, "", this@HackChatFrontend))
            }
        }

        override fun onError(ex: Exception) = throw ex

    }

    fun Map<String, String>.json(): String = JSONValue.toJSONString(this)
}