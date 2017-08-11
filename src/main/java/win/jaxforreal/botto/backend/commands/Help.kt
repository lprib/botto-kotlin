package win.jaxforreal.botto.backend.commands

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.backend.Command
import win.jaxforreal.botto.backend.Commands
import win.jaxforreal.botto.backend.Privilege

class Help : Command("help", Privilege.USER, "get help with a command", onTrigger = {
    //toplevel help with no args
    if (argText == "") {
        val trigger = Config["trigger"]
        replyMessage("${trigger}help \$command-name\n" +
                Commands.commands.map { Config["trigger"] + it.name }.joinToString()
        )
    } else {
        //if a specific command was selected
        val (commandName, _) = getFirstWord(argText)
        try {
            val command = Commands.commands.first { it.name == commandName }
            replyMessage(command.helpText + "\n" + getSubCommandHierarchy(command))
        } catch (e: NoSuchElementException) {
            replyMessage("not command found with the name $commandName")
        }
    }
}) {

    init {

    }

    companion object {
        private fun getSubCommandHierarchy(command: Command): String {
            return command.name +
                    if (command.subCommands.size > 0)
                        "(" + command.subCommands.map { getSubCommandHierarchy(it) }.joinToString() + ")"
                    else ""
        }
    }
}