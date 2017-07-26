package win.jaxforreal.botto

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime

object Log {
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
        return "[$level] ${OffsetDateTime.now()} | $text"
    }
}