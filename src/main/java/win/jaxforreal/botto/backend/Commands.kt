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
                    args.replyMessage("no frontend \"$frontendName\" found")
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
                        val (name, value) = Command.getFirstWord(args.argText)
                        Config[name] = value
                        args.replyMessage("set $name = $value")
                    })
                    .sub(Command("get") { args ->
                        args.replyMessage("property ${args.argText} = ${Config[args.argText]}")
                    })
                    .sub(Command("save") { args ->
                        Config.save()
                        args.replyMessage("saved config file")
                    })
    )
}