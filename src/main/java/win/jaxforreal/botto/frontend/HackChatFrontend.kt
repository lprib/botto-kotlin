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

open class HackChatFrontend(username: String, pass: String, channel: String) : Frontend {
    override val onMessage = Event<MessageEventData>()
    private val parser = JSONParser()

    val ws = object : WebSocketClient(URI(Config["hackchat.url"])) {

        init {
            val context = SSLContext.getInstance("TLS")
            context.init(null, null, null) //null, null, null is the default ssl context
            socket = context.socketFactory.createSocket()
        }

        override fun onOpen(handshake: ServerHandshake?) {
            val text = mapOf("cmd" to "join", "nick" to "$username#$pass", "channel" to channel).json()
            send(text)
        }

        override fun onClose(code: Int, reason: String, remote: Boolean) {
            Log.warn("websocket closed; errorcode:$code reason:\"$reason\" isRemote:$remote")
        }

        override fun onMessage(message: String) {
            val messageData = parser.parse(message) as HashMap<*, *>
            println(messageData)
        }

        override fun onError(ex: Exception) {
            throw ex
        }

    }

    override fun sendMessage(message: String) {
        if (ws.isOpen) {
            val messageJson = mapOf("cmd" to "chat", "text" to message).json()
            ws.send(messageJson)
        } else {
            Log.error("message not sent because websocket is closed `$message`")
        }
    }

    override fun connect() {
        ws.connect()
    }

    override fun disconnect() {
    }

    fun Map<String, String>.json(): String = JSONValue.toJSONString(this)
}