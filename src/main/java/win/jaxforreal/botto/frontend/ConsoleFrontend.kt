package win.jaxforreal.botto.frontend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.Event

class ConsoleFrontend : Frontend {
    override val name: String = "console"
    override val onMessage = Event<MessageEventData>()
    val messageInputThread = Thread {
        while (true) {
            val message = readLine()
            if (message != null) {
                onMessage(MessageEventData(User(Config["console.username"], Config["console.trip"]), message, this))
            }
        }
    }

    override fun sendMessage(message: String) {
        println(message)
    }

    override fun connect() {
        messageInputThread.run()
    }

    override fun disconnect() {
        messageInputThread.interrupt()
    }
}