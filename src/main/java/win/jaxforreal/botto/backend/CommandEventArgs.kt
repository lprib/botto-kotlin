package win.jaxforreal.botto.backend

import win.jaxforreal.botto.frontend.MessageEventData

data class CommandEventArgs(val args: String, val messageData: MessageEventData, val bot: Bot)