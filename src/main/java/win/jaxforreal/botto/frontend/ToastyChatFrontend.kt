package win.jaxforreal.botto.frontend

import win.jaxforreal.botto.frontend.HackChatFrontend

class ToastyChatFrontend(username: String, pass: String, channel: String) : HackChatFrontend(username, pass, channel) {
//    override val ws = object : WebSocketClient(URI(Config["toastychat.ip"])) {
//
//        override fun onOpen(handshake: ServerHandshake) {
//            send("{\"cmd\":\"join\", \"channel\":\"$channel\", \"nick\":\"$username\", \"password\": \"$pass\"}")
//        }
//
//        override fun onClose(code: Int, reason: String, remote: Boolean) {
//        }
//
//        override fun onMessage(message: String) {
//        }
//
//        override fun onError(ex: Exception) {
//        }
//
//    }
}