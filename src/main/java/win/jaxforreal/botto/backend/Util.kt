package win.jaxforreal.botto.backend

import win.jaxforreal.botto.frontend.MessageEventArgs

fun String.replaceVars(messageEventArgs: MessageEventArgs, argText: String):String {
    return this
            .replace("{{user}}", messageEventArgs.user.name)
            .replace("{{trip}}", messageEventArgs.user.trip)
            .replace("{{args}}", argText)
}