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