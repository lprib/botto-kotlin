package win.jaxforreal.botto

import win.jaxforreal.botto.backend.Bot
import win.jaxforreal.botto.frontend.ConsoleFrontend

fun main(args: Array<String>) {
    val bot = Bot()
    val cf = ConsoleFrontend()
    bot.addFrontEnd(cf)
    cf.connect()
    while (true);
}