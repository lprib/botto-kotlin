package win.jaxforreal.botto.backend

import win.jaxforreal.botto.frontend.MessageEventData

data class CommandEventArgs(val argText: String, val messageData: MessageEventData, val bot: Bot) {
    fun replyMessage(message: String) = messageData.frontend.sendMessage(message)
}