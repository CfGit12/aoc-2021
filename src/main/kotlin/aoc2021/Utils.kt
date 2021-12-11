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

fun Coordinate.getSurroundingCoordinates() = listOf(
    Coordinate(x, y - 1), Coordinate(x, y + 1), Coordinate(x + 1, y), Coordinate(x - 1, y)
)

fun Coordinate.getSurroundingCoordinatesIncDiagonals() = listOf(
    Coordinate(x, y - 1), Coordinate(x, y + 1), Coordinate(x + 1, y), Coordinate(x - 1, y),
    Coordinate(x + 1, y + 1), Coordinate(x + 1, y - 1), Coordinate(x - 1, y - 1), Coordinate(x - 1, y + 1),
)


typealias Stack<T> = MutableList<T>
fun <T> Stack<T>.push(item: T) = add(item)
fun <T> Stack<T>.pop(): T? = if (isNotEmpty()) removeAt(lastIndex) else null