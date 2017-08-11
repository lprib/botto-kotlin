package win.jaxforreal.botto.frontend.implementations

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.Event
import win.jaxforreal.botto.backend.Bot
import win.jaxforreal.botto.frontend.Frontend
import win.jaxforreal.botto.frontend.MessageEventArgs
import win.jaxforreal.botto.frontend.User

//NOTE: frontend must be added last so it can hook into the existing frontends
class ConsoleFrontend(bot: Bot) : Frontend {
    init {
        bot.frontends.forEach(this::registerFrontend)
        bot.onFrontendAdd += this::registerFrontend
    }

    override val name: String = "console"
    override val onMessage = Event<MessageEventArgs>()
    override val onUserJoin = Event<User>()
    override val onUserLeave = Event<User>()
    override val onlineUsers = listOf<User>()

    private val messageInputThread = Thread {
        while (true) {
            val message = readLine()
            if (message != null) {
                onMessage(MessageEventArgs(
                        User(
                                Config["console", "username"],
                                Config["console", "trip"], this)
                        , message, this))
            }
        }
    }

    private fun registerFrontend(frontend: Frontend) {
        frontend.onMessage += { (user, text) ->
            println("${user.name}:${user.trip}@${frontend.name} $text")
        }
    }

    override fun sendMessage(message: String) {
        println(message)
    }

    override fun connect(): ConsoleFrontend {
        messageInputThread.start()
        return this
    }

    override fun disconnect() {
        messageInputThread.interrupt()
    }
}