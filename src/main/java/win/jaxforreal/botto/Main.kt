package win.jaxforreal.botto

import win.jaxforreal.botto.backend.Bot
import win.jaxforreal.botto.frontend.ConsoleFrontend
import win.jaxforreal.botto.frontend.User

fun main(args: Array<String>) {
//    println(User("console", "123", ConsoleFrontend()).getPrivilegeLevel().name)
    val bot = Bot()
    val cf = ConsoleFrontend()
    bot.addFrontEnd(cf)
    cf.connect()
    while (true);

}