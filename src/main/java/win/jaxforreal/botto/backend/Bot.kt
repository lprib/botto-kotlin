package win.jaxforreal.botto.backend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.Event
import win.jaxforreal.botto.Log
import win.jaxforreal.botto.frontend.Frontend
import win.jaxforreal.botto.frontend.MessageEventArgs
import java.io.PrintWriter
import java.io.StringWriter


class Bot {
    val frontends = arrayListOf<Frontend>()
    val onFrontendAdd = Event<Frontend>()

    init {
        startDefaultFrontends()
    }

    private fun addFrontend(frontend: Frontend) {
        frontends.add(frontend)
        frontend.onMessage += {
            onMessage(it)
        }

        Log.t("adding frontend:${frontend.name} info:(${frontend.infoString})")
    }

    fun addFrontend(vararg newFrontends: Frontend) {
        newFrontends.forEach { addFrontend(it) }
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

                    ex.printStackTrace()
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

    private fun startDefaultFrontends() {
        val defaultFrontendProps = Config.get<List<Map<String, String>>>("frontend")
        defaultFrontendProps.forEach { addFrontend(FrontendFactory.fromProperties(it, this)!!.connect()) }
    }
}