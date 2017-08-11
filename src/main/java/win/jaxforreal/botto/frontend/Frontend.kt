package win.jaxforreal.botto.frontend

import com.sun.xml.internal.ws.api.message.Message
import win.jaxforreal.botto.Event

interface Frontend {
    val name: String
    val onMessage: Event<MessageEventArgs>
    val onUserJoin: Event<User>
    val onUserLeave: Event<User>
    val onlineUsers: List<User>

    fun sendMessage(message: String)
    fun connect(): Frontend
    fun disconnect()
}