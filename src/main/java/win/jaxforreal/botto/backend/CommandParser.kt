package win.jaxforreal.botto.backend

import win.jaxforreal.botto.frontend.MessageEventData

class Command(
        val name: String,
        val onSuccess: (CommandEventArgs) -> Unit = {}
) {
    private val subCommands = arrayListOf<Command>()

    fun sub(cmd: Command): Command {
        subCommands.add(cmd)
        return this
    }

    /***
     * @param input the string to test, must start with this command to match
     * @return whether or not this or any subCommands matched
     */
    fun test(input: String, data: MessageEventData, bot: Bot): Boolean {
        val words = input.split(Regex.fromLiteral(" "))
        if (name == words[0]) {
            val restOfString = words.drop(1).joinToString(" ")

            //test all subCommands, and stop when a single one matches
            if (!subCommands.any { it.test(restOfString, data, bot) }) {
                onSuccess(CommandEventArgs(restOfString, data, bot))
            }
            return true
        } else return false
    }
}