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
        help("frontend \$frontend-name \$text\n" +
                "broadcasts to the specified frontend")

        sub(Command("all", Privilege.ADMIN) {
            replyMessage("broadcasting to all: $argText")
            bot.broadcastAll(argText)
        }.help("frontend all \$text\n" +
                "broadcasts to all frontends"))

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
        }.help("frontend start \$start-info\n" +
                "start info is parsed by java properties API, commas are replaced by \\n\n" +
                "eg. frontend start type=hackchat, channel=programming, username=IAmABot, password=hunter2\n" +
                "see botto.backend.FrontendFactory.kt for start info parsing code"))
    }
}