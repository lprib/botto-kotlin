package win.jaxforreal.botto.backend

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.backend.Privilege.*
import win.jaxforreal.botto.backend.commands.FrontendManager
import win.jaxforreal.botto.backend.commands.Help
import kotlin.system.exitProcess

object Commands {
    val commands = mutableListOf(
            Command("config", ADMIN)

                    .sub(Command("set", ADMIN) {
                        val (path, value) = Command.getFirstWord(argText)
                        val pathSplit = path.split(".").toTypedArray()
                        Config.set(keys = *pathSplit, value = value)
                        replyMessage("Set $path = $value")
                    }.help("config set \$path.to.item \$value"))

                    .sub(Command("get", ADMIN) {
                        val pathSplit = argText.split(".").toTypedArray()
                        replyMessage("${argText} = ${Config.getMaybe<Any>(*pathSplit)}")
                    }.help("config get \$path.to.item"))

                    .sub(Command("save", ADMIN) {
                        Config.save()
                        replyMessage("Config saved.")
                    }.help("saves config to userConfig.txt")),


            Command("privilege", ADMIN)

                    .sub(Command("set", ADMIN) {
                        val (name, trip, frontend, level) = argText.split(" ")
                        val levelList = Config.get<MutableList<Map<String, Any>>>("privilege", level.toUpperCase())
                        levelList.add(mapOf("name" to name, "trip" to trip, "frontend" to frontend))
                        replyMessage("User $name added to unsaved config as a(n) $level")
                    }.help("privilege set \$username \$tripcode \$frontend-name \$privilege-level")),


            Command("test") {
                replyMessage("argText=$argText\n" +
                        "fullText=${messageArgs.text}\n" +
                        "user=${messageArgs.user.name} ${messageArgs.user.trip}\n" +
                        "userPrivilegeLevel=${messageArgs.user.getPrivilegeLevel()}\n" +
                        "frontend=${messageArgs.frontend.name}\n" +
                        "frontendInfoString=(${messageArgs.frontend.infoString})")
            },

            Command("stop", ADMIN) {
                //System.exit(0)
                replyMessage("nice try")
            }.help("terminates bot and all frontends"),

            Help(),
            FrontendManager(),

            Command("command", ADMIN)
                    .sub(Command("add", ADMIN) {
                        val (commandName, commandText) = Command.getFirstWord(argText)
                        Config["commands", commandName] = commandText
                        addCommand(getStringCommand(commandName, commandText))
                    }.help("command add \$command-name \$command-text\n" +
                            "available variables: {{user}} {{trip}} {{args}}"))
                    .sub(Command("remove", ADMIN) {
                        Config.get<MutableMap<String, String>>("commands").remove("argText")
                        removeCommand(argText)
                    }.help("command remove \$command-name"))
    )

    init {
        loadConfigTextCommands()
    }

    private fun getStringCommand(name: String, replyText: String): Command =
            Command(name, USER) {
                replyMessage(replyText.replaceVars(messageArgs, argText))
            }

    private fun loadConfigTextCommands() {
        Config.get<Map<String, String>>("commands")
                .map { getStringCommand(it.key, it.value) }
                .forEach(this::addCommand)
    }

    //these need to be separate functions otherwise it causes an internal compiler exception :O
    private fun addCommand(c: Command) {
        commands.add(c)
    }

    private fun removeCommand(name: String) {
        commands.remove(commands.find { it.name == name })
    }
}