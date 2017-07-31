package win.jaxforreal.botto.backend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.frontend.Frontend
import win.jaxforreal.botto.frontend.MessageEventArgs
import java.io.PrintWriter
import java.io.StringWriter


class Bot {
    val frontends = arrayListOf<Frontend>()

    fun addFrontEnd(f: Frontend) {
        frontends.add(f)
        f.onMessage += {
            onMessage(it)
        }
    }

    private fun onMessage(messageArgs: MessageEventArgs) {
        if (messageArgs.text.startsWith(Config["trigger"])) {
            val withoutPrefix = messageArgs.text.substring(Config["trigger"].length)
            try {
                Commands.commands.first { it.test(withoutPrefix, messageArgs, this) }
            } catch (ex: Throwable) {
                //NoSuchElementException is normal when a command is not recognised
                if (ex !is NoSuchElementException) {
                    val sw = StringWriter()
                    ex.printStackTrace(PrintWriter(sw))
                    messageArgs.frontend.sendMessage(sw.toString())
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