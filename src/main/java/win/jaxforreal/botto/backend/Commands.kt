package win.jaxforreal.botto.backend

object Commands {
    val commands = arrayOf(
        Command("source") { args ->
            println("sdf")
            args.messageData.frontend.sendMessage("no source for you, ${args.messageData.user}")
        }
    )
}