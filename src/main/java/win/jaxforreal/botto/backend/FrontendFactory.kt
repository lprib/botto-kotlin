package win.jaxforreal.botto.backend

import win.jaxforreal.botto.frontend.Frontend
import win.jaxforreal.botto.frontend.implementations.ConsoleFrontend
import win.jaxforreal.botto.frontend.implementations.HackChatFrontend
import win.jaxforreal.botto.frontend.implementations.ToastyChatFrontend
import java.io.StringReader
import java.util.*

object FrontendFactory {
    @Suppress("UNCHECKED_CAST")
    fun fromString(props: String, bot: Bot): Frontend? {
        val propsMap = Properties()
        propsMap.load(StringReader(props.replace(',', '\n')))
        return fromProperties(propsMap.toMap() as Map<String, String>, bot)
    }

    fun fromProperties(props: Map<String, String>, bot: Bot): Frontend? {
        when (props["type"]) {
            "hackchat" -> {
                return if (props.checkKeys("channel", "username", "password"))
                    HackChatFrontend(props["username"]!!, props["password"]!!, props["channel"]!!)
                else null
            }

            "toastychat" -> {
                return if (props.checkKeys("channel", "username", "password"))
                    ToastyChatFrontend(props["username"]!!, props["password"]!!, props["channel"]!!)
                else null
            }

            "irc" -> {
                return null
            }

            "console" -> {
                return ConsoleFrontend(bot)
            }

            else -> {
                return null
            }
        }
    }

    private fun Map<String, Any>.checkKeys(vararg keys: String): Boolean {
        return keys.all { this.containsKey(it) }
    }
}