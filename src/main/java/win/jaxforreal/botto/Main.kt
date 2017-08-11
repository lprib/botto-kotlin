package win.jaxforreal.botto

import win.jaxforreal.botto.backend.Bot
import win.jaxforreal.botto.frontend.implementations.ConsoleFrontend
import win.jaxforreal.botto.frontend.implementations.HackChatFrontend
import win.jaxforreal.botto.frontend.implementations.ToastyChatFrontend

//import win.jaxforreal.botto.frontend.ToastyChatFrontend

fun main(args: Array<String>) {
//    val bot = Bot()
//    bot.addFrontend(ConsoleFrontend(bot).connect())

    val bot = Bot()
    bot.addFrontend(
            ConsoleFrontend(bot).connect(),
            HackChatFrontend("botto", "asd", "botDev").connect()
    )
    while (true);
}