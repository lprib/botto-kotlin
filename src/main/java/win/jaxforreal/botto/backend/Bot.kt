package win.jaxforreal.botto.backend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.frontend.ConsoleFrontend
import win.jaxforreal.botto.frontend.Frontend
import win.jaxforreal.botto.frontend.MessageEventData

class Bot {
    val frontends = arrayListOf<Frontend>()

    fun addFrontEnd(f: Frontend) {
        frontends.add(f)
        f.onMessage += this::onMessage
    }

    private fun onMessage(messageData: MessageEventData) {
        if (messageData.text.startsWith(Config["trigger"] as String)) {
            val withoutPrefix = messageData.text.substring(1)
            Commands.commands.first { it.test(withoutPrefix, messageData, this) }
        }
    }

    fun broadcastAll(message: String) {
        frontends.forEach { frontend -> frontend.sendMessage(message) }
    }

    /**
     * @return whether there was a frontend of specified type available to broadcast to
     */
    fun broadcastTo(frontendName: String, message: String): Boolean {
        val matchingFrontends = frontends.filter { it.name == frontendName }
        matchingFrontends.forEach { it.sendMessage(message) }

        return matchingFrontends.isNotEmpty()
    }
}