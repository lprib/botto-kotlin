package win.jaxforreal.botto.frontend.implementations

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.json.simple.JSONValue
import org.json.simple.parser.JSONParser
import win.jaxforreal.botto.Config
import win.jaxforreal.botto.Event
import win.jaxforreal.botto.Log
import win.jaxforreal.botto.frontend.Frontend
import win.jaxforreal.botto.frontend.MessageEventArgs
import win.jaxforreal.botto.frontend.User
import java.lang.Exception
import java.net.URI
import javax.net.ssl.SSLContext

open class HackChatFrontend(val username: String, val pass: String, val channel: String) : Frontend {
    override val name = "hackchat"
    override val infoString = "username=$username channel=$channel"

    override val onMessage = Event<MessageEventArgs>()
    override val onUserJoin = Event<User>()
    override val onUserLeave = Event<User>()
    override val onlineUsers = ArrayList<User>()

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
        Log.t("connecting hc...")
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

            Log.t("websocket opened")
        }

        override fun onClose(code: Int, reason: String, remote: Boolean) {
            Log.warn("websocket closed; errorcode:$code reason:\"$reason\" isRemote:$remote")
        }

        @Suppress("UNCHECKED_CAST")
        override fun onMessage(message: String) {
            val data = parser.parse(message) as Map<String, Any>
            if (data["cmd"] == "chat") {
                val chattingUser = User(data["nick"] as String, data["trip"] as String? ?: "", this@HackChatFrontend)
                this@HackChatFrontend.onMessage(
                        MessageEventArgs(
                                chattingUser, data["text"] as String, this@HackChatFrontend
                        )
                )

                //if the chatting user has a trip, update it in the onlineUsers list
                if (chattingUser.trip != "") {
                    onlineUsers.forEachIndexed { index, (onlineUserName) ->
                        if (chattingUser.name == onlineUserName) {
                            onlineUsers[index] = chattingUser
                        }
                    }
                }

            } else if (data["cmd"] == "onlineAdd") {
                val user = User(data["nick"] as String, "", this@HackChatFrontend)
                onUserJoin(user)
                onlineUsers += user
            } else if (data["cmd"] == "onlineRemove") {
                val leavingUser = User(data["nick"] as String, "", this@HackChatFrontend)
                onUserLeave(leavingUser)
                //remove from onlineUsers, ignoring the tripcode, because it may not be known at this time
                for ((userName) in onlineUsers) {
                    if (userName == leavingUser.name) {
                        onlineUsers -= leavingUser
                        return
                    }
                }
            } else {
                Log.warn("unrecognised JSON: $message")
            }
        }

        override fun onError(ex: Exception) = throw ex

    }

    fun Map<String, String>.json(): String = JSONValue.toJSONString(this)
}