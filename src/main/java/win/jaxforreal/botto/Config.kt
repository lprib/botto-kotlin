package win.jaxforreal.botto

import java.util.*

object Config : Properties() {
    init {
        val stream = this.javaClass.classLoader.getResourceAsStream("config.properties")
        load(stream)
    }

    operator fun get(name: String): String = getProperty(name)
}