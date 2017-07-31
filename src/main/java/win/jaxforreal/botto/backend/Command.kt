package win.jaxforreal.botto.backend

import win.jaxforreal.botto.frontend.MessageEventArgs

class Command(
        val name: String,
        val privilege: Privilege = Privilege.USER,
        val helpText: String = "no help",
        val onSuccess: CommandEventArgs.() -> Unit = {}
) {
    private val subCommands = arrayListOf<Command>()

    fun help(text: String) = Command(name, privilege, text, onSuccess)

    fun sub(sub: Command): Command {
        subCommands.add(sub)
        return this
    }

    /***
     * @param input the string to test, must start with this command to match
     * @return whether or not this or any subCommands matched
     */
    fun test(input: String, args: MessageEventArgs, bot: Bot): Boolean {
        val (firstWord, restOfString) = getFirstWord(input)
        if (name == firstWord) {
            //test all subCommands, and stop when a single one matches
            if (!subCommands.any { it.test(restOfString, args, bot) }) {
                val commandEventArgs = CommandEventArgs(restOfString, args, bot)

                //if none match, and privilege levels are OK, the parent command is triggered
                if (args.user.getPrivilegeLevel().isAtOrAbove(privilege)) {
                    onSuccess(commandEventArgs)
                } else {
                    commandEventArgs.replyMessage("You must have privilege level $privilege to do that :)")
                }
            }
            return true
        } else return false
    }

    companion object {
        fun getFirstWord(input: String): Pair<String, String> {
            val first = input.split(" ")[0]
            val rest = if (input.length > first.length + 1) input.substring(first.length + 1) else ""
            return Pair(first, rest)
        }
    }
}