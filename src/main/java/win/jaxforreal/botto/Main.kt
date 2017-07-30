package win.jaxforreal.botto

import win.jaxforreal.botto.backend.Bot
import win.jaxforreal.botto.frontend.ConsoleFrontend
import win.jaxforreal.botto.frontend.HackChatFrontend
import win.jaxforreal.botto.frontend.User

fun main(args: Array<String>) {
//    println(User("console", "123", ConsoleFrontend()).getPrivilegeLevel().name)
    val bot = Bot()
    val hackChatFrontend = HackChatFrontend("botto", "asd", "botDev")
    val consoleFrontend = ConsoleFrontend(bot)

    bot.addFrontEnd(hackChatFrontend)
    bot.addFrontEnd(consoleFrontend)

    hackChatFrontend.connect()
    consoleFrontend.connect()
    while (true);

}