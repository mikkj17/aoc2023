package utils

fun <T> List<List<T>>.transpose(): List<List<T>> {
    if (isEmpty() || this[0].isEmpty()) return this

    val width = this[0].size

    return (0..<width).map { i ->
        this.map { it[i] }
    }
}

@JvmName("transposeListOfStrings")
fun List<String>.transpose(): List<String> {
    if (isEmpty() || this[0].isEmpty()) return this

    val width = this[0].length

    return (0..<width).map { i ->
        this.map { it[i] }.joinToString("")
    }
}
