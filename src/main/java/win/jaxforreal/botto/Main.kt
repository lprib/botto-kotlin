package win.jaxforreal.botto

import win.jaxforreal.botto.backend.Bot
import win.jaxforreal.botto.backend.Command
import win.jaxforreal.botto.frontend.ConsoleFrontend
import win.jaxforreal.botto.frontend.HackChatFrontend

fun main(args: Array<String>) {
    val bot = Bot()
    val cf = ConsoleFrontend()
    bot.addFrontEnd(cf)
    cf.connect()
    while(true);
}