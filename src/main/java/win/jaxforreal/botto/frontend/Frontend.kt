package win.jaxforreal.botto.frontend

import win.jaxforreal.botto.Event

interface Frontend {
    val name: String
    val onMessage: Event<MessageEventArgs>
    val onUserJoin: Event<User>
    val onUserLeave: Event<User>

    fun sendMessage(message: String)
    fun connect(): Frontend
    fun disconnect()
}