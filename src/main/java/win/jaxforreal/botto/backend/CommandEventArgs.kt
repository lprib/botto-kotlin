package win.jaxforreal.botto.backend

import win.jaxforreal.botto.frontend.MessageEventArgs

data class CommandEventArgs(val argText: String, val messageArgs: MessageEventArgs, val bot: Bot) {
    fun replyMessage(message: String) = messageArgs.frontend.sendMessage(message)
}