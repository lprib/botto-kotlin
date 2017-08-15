package win.jaxforreal.botto

import java.time.OffsetDateTime

object Log {

    fun t(text: String) {
        println(getMessage("trace", text))
    }

    fun info(text: String) {
        println(getMessage("info", text))
    }

    fun warn(text: String) {
        println(getMessage("warn", text))
    }

    fun error(text: String) {
        println(getMessage("error", text))
    }

    private fun getMessage(level: String, text: String):String {
        return "[${level.padStart(5)}] ${OffsetDateTime.now()} | $text"
    }
}