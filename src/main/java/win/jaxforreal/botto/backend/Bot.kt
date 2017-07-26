package win.jaxforreal.botto.backend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.frontend.Frontend
import win.jaxforreal.botto.frontend.MessageEventData

class Bot {
    private val frontends = arrayListOf<Frontend>()

    fun addFrontEnd(f: Frontend) {
        frontends.add(f)
        f.onMessage += this::onMessage
    }

    private fun onMessage(messageData: MessageEventData) {
        if (messageData.text.startsWith(Config["trigger"])) {
            val withoutPrefix = messageData.text.substring(1)
            for (command in Commands.commands) {
                if (command.test(withoutPrefix, messageData, this)) return
            }
        }
    }

    fun broadcast(message: String) {
        frontends.forEach { frontend -> frontend.sendMessage(message) }
    }
}