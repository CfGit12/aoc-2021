package aoc2021

fun readResourceFile(name: String) = object {}::class.java.classLoader.getResource(name)!!.readText()

fun readResourceFileAsLines(name: String) = readResourceFile(name).lines()

fun readResourceFileAsIntLine(name: String) =
    readResourceFileAsLines(name)
        .first()
        .split(",")
        .map { it.toInt() }

data class Coordinate(
    val x: Int,
    val y: Int
)

typealias Stack<T> = MutableList<T>
fun <T> Stack<T>.push(item: T) = add(item)
fun <T> Stack<T>.pop(): T? = if (isNotEmpty()) removeAt(lastIndex) else null