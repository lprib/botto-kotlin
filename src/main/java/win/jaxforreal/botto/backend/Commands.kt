package win.jaxforreal.botto.backend

import win.jaxforreal.botto.Config

object Commands {
    val commands = arrayOf(

            Command("source") { args ->
                args.messageData.frontend.sendMessage("no source for you, @${args.messageData.user.name}")
            },


            Command("broadcast") { args ->
                val (frontendName, text) = Command.getFirstWord(args.argText)
                if (args.bot.broadcastTo(frontendName, text)) {
                    println("broadcasting to $frontendName: `$text`")
                } else {
                    args.replyMessage("no frontend '$frontendName' found")
                }
            }
                    .sub(Command("all") { (argText, _, bot) ->
                        println("broadcasting all")
                        bot.broadcastAll(argText)
                    })
                    .sub(Command("list") { args ->
                        val list = args.bot.frontends.map { it.name }.joinToString()
                        args.replyMessage("Available frontends: $list.")
                    }),


            Command("config")
                    .sub(Command("set") { args ->
                        val (path, value) = Command.getFirstWord(args.argText)
                        val pathSplit = path.split(".").toTypedArray()
                        Config.set(keys = *pathSplit, value = value)
                        args.replyMessage("Set $path = $value")
                    })
                    .sub(Command("get") { args ->
                        val pathSplit = args.argText.split(".").toTypedArray()
                        args.replyMessage("${args.argText} = ${Config.getMaybe<Any>(*pathSplit)}")
                    })
                    .sub(Command("save") { args ->
                        Config.save()
                        args.replyMessage("Config saved.")
                    })
    )
}