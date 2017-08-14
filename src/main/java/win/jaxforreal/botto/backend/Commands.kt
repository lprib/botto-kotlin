package win.jaxforreal.botto.backend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.backend.Privilege.*
import win.jaxforreal.botto.backend.commands.FrontendManager
import win.jaxforreal.botto.backend.commands.Help
import kotlin.system.exitProcess

object Commands {
    val commands = arrayOf(

            Command("source") {
                messageArgs.frontend.sendMessage("no source for you, @${messageArgs.user.name}")
            },

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
                        "fullText=${messageArgs.text}\n" +
                        "user=${messageArgs.user.name} ${messageArgs.user.trip}\n" +
                        "frontend=${messageArgs.frontend}\n" +
                        "frontendInfoString=(${messageArgs.frontend.infoString})")
            },

            Command("stop", ADMIN) {
                System.exit(0)
            },

            Help(),
            FrontendManager()
    )
}