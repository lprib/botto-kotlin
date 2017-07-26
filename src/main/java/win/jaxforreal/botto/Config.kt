package win.jaxforreal.botto

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

object Config : Properties(DefaultProperties) {
    private val userPropFile = File(File(javaClass.protectionDomain.codeSource.location.toURI()), "config.properties")

    init {
        if (userPropFile.isFile) {
            load(FileReader(userPropFile))
        }
    }

    operator fun get(name: String): String = getProperty(name)

    operator fun set(name: String, value: String) = setProperty(name, value)

    fun save() {
        store(FileWriter(userPropFile), null)
    }
}

private object DefaultProperties : Properties() {
    init {
        val stream = javaClass.classLoader.getResourceAsStream("defaultConfig.properties")
        load(stream)
    }
}