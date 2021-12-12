package aoc2021

typealias Position = String

private val input: Map<Position, List<Position>> = buildMap{
    readResourceFileAsLines("12.txt")
        .map {
            val (start, finish) = it.split("-")
            merge(start, listOf(finish), List<Position>::plus)
            merge(finish, listOf(start), List<Position>::plus)
        }
}

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() = getAllPaths(visitableTwice = false).size
private fun part2() = getAllPaths(visitableTwice = true).size

private fun getAllPaths(visitableTwice: Boolean): Set<List<Position>> {
    val endPaths = mutableSetOf<List<Position>>()

    fun walk(position: Position, path: List<Position> = emptyList(), hadSecondVisit: Boolean = false) {
        val updatedPath = path + position

        if (position == "end") {
            endPaths.add(path)
        } else {
            val positionsToWalk = input[position]?.filter {
                if (visitableTwice && !hadSecondVisit) {
                    it != "start"
                } else {
                    it.isUpperCase() || it !in path
                }
            } ?: emptyList()

            for (positionToWalk in positionsToWalk) {
                val updatedSecondVisit = (positionToWalk.isLowerCase() && positionToWalk in path) || hadSecondVisit
                walk(positionToWalk, updatedPath, updatedSecondVisit)
            }
        }
    }

    walk("start")

    return endPaths
}

private fun String.isLowerCase() = this.lowercase() == this
private fun String.isUpperCase() = this.uppercase() == this