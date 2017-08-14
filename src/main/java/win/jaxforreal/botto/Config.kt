package win.jaxforreal.botto

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

object Config {
    private val userPropFile = File(File(javaClass.protectionDomain.codeSource.location.toURI()), "userConfig.txt")
    private val propsToml = Toml().read(FileReader(javaClass.classLoader.getResource("defaults.txt").file))

    //load user config file into props if it exists
    val props: MutableMap<String, Any> = if (userPropFile.isFile) {
        propsToml.read(FileReader(userPropFile)).toMap()
    } else {
        propsToml.toMap()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getMaybe(vararg keys: String): T? {
        var nextMap = props

        for (key in keys.dropLast(1)) {
            nextMap = nextMap[key] as MutableMap<String, Any>
        }

        return nextMap[keys.last()] as T
    }

    /**
     * gets the value of type T at the path defined by keys
     *
     * eg. Config.get<String>("console", "username") would return "qwerty" with the following TOML:
     *
     * [ config]
     * username = "qwerty"
     */
    operator fun get(vararg keys: String): String = getMaybe<String>(*keys)!!

    /**
     * generic version of the default get
     */
    operator fun <T> get(vararg keys: String): T = getMaybe<T>(*keys)!!

    /**
     * Config["irc", "username"] = value
     */
    @Suppress("UNCHECKED_CAST")
    operator fun set(vararg keys: String, value: Any) {
        var nextMap = props
        for (key in keys.dropLast(1)) {
            nextMap = if (nextMap.containsKey(key)) {
                //return the inner map if it exists
                nextMap[key] as MutableMap<String, Any>
            } else {
                //if the inner map doesn't exist, create and return it
                nextMap[key] = mutableMapOf<String, Any>()
                nextMap[key] as MutableMap<String, Any>
            }
        }

        nextMap[keys.last()] = value
    }

    /**
     * Save properties to the user file "userConfig.txt",
     * located in the same directory as this JAR/classpath.
     */
    fun save() {
        val writer = FileWriter(userPropFile, false)
        TomlWriter().write(props, writer)
        writer.close()
    }
}