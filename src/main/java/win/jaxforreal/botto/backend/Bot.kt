package win.jaxforreal.botto.backend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.frontend.Frontend
import win.jaxforreal.botto.frontend.MessageEventData
import kotlin.reflect.jvm.internal.impl.utils.ExceptionUtilsKt
import java.io.PrintWriter
import java.io.StringWriter


class Bot {
    val frontends = arrayListOf<Frontend>()

    fun addFrontEnd(f: Frontend) {
        frontends.add(f)
        f.onMessage += this::onMessage
    }

    private fun onMessage(messageData: MessageEventData) {
        if (messageData.text.startsWith(Config["trigger"])) {
            val withoutPrefix = messageData.text.substring(Config["trigger"].length)
            try {
                Commands.commands.first { it.test(withoutPrefix, messageData, this) }
            } catch (ex: Throwable) {
                //NoSuchElementException is normal when a command is not recognised
                if (ex !is NoSuchElementException) {
                    val sw = StringWriter()
                    ex.printStackTrace(PrintWriter(sw))
                    messageData.frontend.sendMessage(sw.toString())
                }
            }
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