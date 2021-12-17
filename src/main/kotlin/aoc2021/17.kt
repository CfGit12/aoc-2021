package aoc2021

private val input =
    readResourceFile("17.txt").let {
        val withStartDropped = it.replace("target area: ", "")
        val (xString, yString) = withStartDropped.split(", ")
        fun String.parseRange(): IntRange {
            val (_, rangeString) = split("=")
            val (lower, upper) = rangeString.split("..")
            return lower.toInt()..upper.toInt()
        }
        xString.parseRange() to yString.parseRange()
    }

fun main() {
    println(input)
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1(): Int {
    var highest = 0
    for (x in 1..25) {
        for (y in 1..150) {
            val height = findHighestY(x, y)
            if (height != null && height > highest) highest = height
        }
    }
    return highest
}

private fun part2(): Int {
    val validVelocities = mutableSetOf<Pair<Int, Int>>()
    for (x in 1..184) {
        for (y in -125..150) {
            val coordinates = fireProbe(x, y)
            if (coordinates != null) validVelocities.add(x to y)
        }
    }
    return validVelocities.size
}

private fun findHighestY(xVelocity: Int, yVelocity: Int): Int? =
    fireProbe(xVelocity, yVelocity)?.maxOf { it.y }

private fun fireProbe(
    xVelocity: Int,
    yVelocity: Int,
    position: Coordinate = Coordinate(0, 0),
    allPositions: List<Coordinate> = listOf(position),
    step: Int = 1
): List<Coordinate>? {

    val newPosition = position.copy(
        x = position.x + xVelocity, y = position.y + yVelocity
    )
    val newPositions = allPositions + newPosition

    if (newPosition.x > input.first.last || newPosition.y < input.second.first) {
        //println("Overshot on step $step - $newPosition")
        return null
    } // Overshot

    if (newPosition.x in input.first && newPosition.y in input.second) return newPositions

    val newXVelocity =
        if (xVelocity > 0) xVelocity - 1 else if (xVelocity < 0) xVelocity + 1 else xVelocity
    val newYVelocity = yVelocity - 1

    return fireProbe(newXVelocity, newYVelocity, newPosition, newPositions, step + 1)
}
