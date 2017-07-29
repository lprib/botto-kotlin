package win.jaxforreal.botto.backend

enum class Privilege {
    USER,
    MODERATOR,
    ADMIN;

    fun isAtOrAbove(other: Privilege): Boolean {
        return values().indexOf(this) >= values().indexOf(other)
    }
}