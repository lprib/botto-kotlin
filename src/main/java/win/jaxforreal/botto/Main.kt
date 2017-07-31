package win.jaxforreal.botto

import win.jaxforreal.botto.backend.Bot
import win.jaxforreal.botto.frontend.implementations.ConsoleFrontend
import win.jaxforreal.botto.frontend.implementations.HackChatFrontend
//import win.jaxforreal.botto.frontend.ToastyChatFrontend

fun main(args: Array<String>) {
    println(Config["toastychat", "url"])
    val bot = Bot()
    val hackChatFrontend = HackChatFrontend("botto", "asd", "botDev")
    val consoleFrontend = ConsoleFrontend(bot)
//    val toastyFrontend = ToastyChatFrontend("qweqwe", "asd", "botDev")

//    bot.addFrontEnd(toastyFrontend)
    bot.addFrontEnd(hackChatFrontend)
    bot.addFrontEnd(consoleFrontend)

    hackChatFrontend.connect()
    consoleFrontend.connect()
//    toastyFrontend.connect()
    while (true);

}