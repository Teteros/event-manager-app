package utils

object Utilities {

    @JvmStatic
    fun isValidString(input: String): Boolean {
        return (input != "")
    }

    @JvmStatic
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    @JvmStatic
    fun isValidInteger(input: Int): Boolean {
        return (input < 1)
    }
}
