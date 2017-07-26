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
        val (firstWord, restOfString) = getFirstWord(input)
        if (name == firstWord) {
            //test all subCommands, and stop when a single one matches
            if (!subCommands.any { it.test(restOfString, data, bot) }) {
                onSuccess(CommandEventArgs(restOfString, data, bot))
            }
            return true
        } else return false
    }

    companion object {
        fun getFirstWord(input: String): Pair<String, String> {
            val first = input.split(Regex.fromLiteral(" "))[0]
            val rest = if (input.length > first.length + 1) input.substring(first.length + 1) else ""
            return Pair(first, rest)
        }
    }
}