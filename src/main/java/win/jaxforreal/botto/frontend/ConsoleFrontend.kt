package win.jaxforreal.botto.frontend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.Event
import win.jaxforreal.botto.backend.Bot

//NOTE: frontend must be added last so it can hook into the existing frontends
class ConsoleFrontend(bot: Bot) : Frontend {
    init {
        bot.frontends.forEach {
            it.onMessage += { (user, text) ->
                println("${user.name}:${user.trip}@${it.name} ${text}")
            }
        }
    }

    override val name: String = "console"
    override val onMessage = Event<MessageEventArgs>()
    override val onUserJoin = Event<User>()
    override val onUserLeave = Event<User>()

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

    override fun sendMessage(message: String) {
        println(message)
    }

    override fun connect(): ConsoleFrontend {
        messageInputThread.run()
        return this
    }

    override fun disconnect() {
        messageInputThread.interrupt()
    }
}