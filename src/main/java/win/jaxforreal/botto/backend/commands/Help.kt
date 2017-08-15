package win.jaxforreal.botto.backend.commands

import win.jaxforreal.botto.Config
import win.jaxforreal.botto.backend.Command
import win.jaxforreal.botto.backend.Commands
import win.jaxforreal.botto.backend.Privilege

class Help : Command("help", Privilege.USER, onTrigger = {
    //toplevel help with no args
    if (argText == "") {
        val trigger = Config["trigger"]
        replyMessage("@${messageArgs.user.name} ${trigger}help \$command-name [\$subcommand-name]\n" +
                Commands.commands.map { Config["trigger"] + it.name + " (${it.privilege.name})" }.joinToString()
        )
    } else {
        //if a specific command was selected
        val commandNames = argText.split(" ")
        try {
            var command = Commands.commands.first { it.name == commandNames[0] }
            for (nestedCommandName in commandNames.drop(1)) {
                command = command.subCommands.first{it.name == nestedCommandName}
            }

            replyMessage("${command.helpText}\nhierarchy: ${getSubCommandHierarchy(command)}")
        } catch (e: NoSuchElementException) {
            replyMessage("not command found with the name $argText")
        }
    }
}) {

    init {
        help("help \$command-name [\$subcommand-name]")
    }

    //TODO test
    companion object {
        private fun getSubCommandHierarchy(command: Command): String {
            return if (command.subCommands.size > 0)
                        command.name + " -> (" + command.subCommands.map { getSubCommandHierarchy(it) }.joinToString() + ")"
                    else command.name + "(no hierarchy)"
        }
    }
}