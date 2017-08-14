package win.jaxforreal.botto.backend.commands

import win.jaxforreal.botto.backend.Command
import win.jaxforreal.botto.backend.FrontendFactory
import win.jaxforreal.botto.backend.Privilege
import java.io.StringReader
import java.util.*

class FrontendManager : Command("frontend", Privilege.ADMIN, onTrigger = {
    val (frontendName, text) = Command.getFirstWord(argText)
    if (bot.broadcastTo(frontendName, text)) {
        replyMessage("broadcasting to $frontendName: `$text`")
    } else {
        replyMessage("no frontend '$frontendName' found")
    }
}) {

    init {
        help("broadcasts to the specified frontend\n" +
                "frontend \$frontend-name \$text")

        sub(Command("all", Privilege.ADMIN) {
            replyMessage("broadcasting to all: $argText")
            bot.broadcastAll(argText)
        })

        sub(Command("list", Privilege.MODERATOR) {
            val list = bot.frontends.map { "${it.name}(${it.infoString})" }.joinToString()
            replyMessage("Available frontends: $list.")
        })

        sub(Command("start", Privilege.ADMIN) {
            val newFrontend = FrontendFactory.fromString(argText, bot)
            if (newFrontend != null) {
                bot.addFrontend(newFrontend)
                replyMessage("added frontend: $newFrontend")
                newFrontend.connect()
            } else {
                replyMessage("could not add frontend")
            }
        })
    }
}