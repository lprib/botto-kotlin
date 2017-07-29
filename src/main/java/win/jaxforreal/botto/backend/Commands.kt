package win.jaxforreal.botto.backend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.backend.Privilege.*

object Commands {
    val commands = arrayOf(

            Command("source") {
                messageData.frontend.sendMessage("no source for you, @${messageData.user.name}")
            },


            Command("broadcast", MODERATOR) {
                val (frontendName, text) = Command.getFirstWord(argText)
                if (bot.broadcastTo(frontendName, text)) {
                    println("broadcasting to $frontendName: `$text`")
                } else {
                    replyMessage("no frontend '$frontendName' found")
                }
            }
                    .help("")

                    .sub(Command("all", MODERATOR) {
                        println("broadcasting all")
                        bot.broadcastAll(argText)
                    })
                    .sub(Command("list", MODERATOR) {
                        val list = bot.frontends.map { it.name }.joinToString()
                        replyMessage("Available frontends: $list.")
                    }),


            Command("config", ADMIN)
                    .sub(Command("set", ADMIN) {
                        val (path, value) = Command.getFirstWord(argText)
                        val pathSplit = path.split(".").toTypedArray()
                        Config.set(keys = *pathSplit, value = value)
                        replyMessage("Set $path = $value")
                    })
                    .sub(Command("get", ADMIN) {
                        val pathSplit = argText.split(".").toTypedArray()
                        replyMessage("${argText} = ${Config.getMaybe<Any>(*pathSplit)}")
                    })
                    .sub(Command("save", ADMIN) {
                        Config.save()
                        replyMessage("Config saved.")
                    }),
            //TODO
            Command("privilege", ADMIN)
    )
}