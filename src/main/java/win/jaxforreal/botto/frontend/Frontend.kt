package win.jaxforreal.botto.frontend

import win.jaxforreal.botto.Event

interface Frontend {
    val name: String
    val onMessage: Event<MessageEventData>

    fun sendMessage(message: String)
    fun connect()
    fun disconnect()
}