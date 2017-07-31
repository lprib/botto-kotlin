package win.jaxforreal.botto.frontend

import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import win.jaxforreal.botto.Config
import win.jaxforreal.botto.Log
import java.lang.Exception
import java.net.URI
//
//class ToastyChatFrontend(username: String, pass: String, channel: String) {
//    override val joinInfoMap get() = mapOf("cmd" to "join", "nick" to username, "pass" to pass, "channel" to channel)
//    override val joinURI get() = URI(Config["toastychat", "url"])
//    override val name = "toastychat"
//
//    override fun doSSLSetup(ws: WebSocketClient) {
//        //no ssl setup required because of cloudflare protection
//        println(joinInfoMap.json())
//    }
//}