package win.jaxforreal.botto.backend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.backend.Privilege.*
import win.jaxforreal.botto.backend.commands.Help

object Commands {
    val commands = arrayOf(

            Command("source") {
                messageArgs.frontend.sendMessage("no source for you, @${messageArgs.user.name}")
            },


            Command("broadcast", MODERATOR) {
                val (frontendName, text) = Command.getFirstWord(argText)
                if (bot.broadcastTo(frontendName, text)) {
                    replyMessage("broadcasting to $frontendName: `$text`")
                } else {
                    replyMessage("no frontend '$frontendName' found")
                }
            }
                    .help("broadcasts to the specified frontend\n" +
                            "broadcast \$frontend-name \$text")

                    .sub(Command("all", MODERATOR) {
                        replyMessage("broadcasting to all: $argText")
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

            Command("privilege", ADMIN)
                    .sub(Command("set", ADMIN) {
                        val (name, trip, frontend, level) = argText.split(" ")
                        val levelList = Config.get<MutableList<Map<String, Any>>>("privilege", level.toUpperCase())
                        levelList.add(mapOf("name" to name, "trip" to trip, "frontend" to frontend))
                        replyMessage("User $name added to unsaved config as a(n) $level")
                    }),
            Command("test") {
                replyMessage("argText=$argText\n" +
                        "fullText=${messageArgs.text}" +
                        "user=${messageArgs.user.name} ${messageArgs.user.trip}\n" +
                        "frontend=${messageArgs.frontend}")
            },

            Help()
    )
}