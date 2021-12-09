package aoc2021

private val input = buildMap {
    readResourceFileAsLines("9.txt")
        .mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                put(Coordinate(x, y), c.digitToInt())
            }
        }
}

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() =
    input
        .getLowPoints()
        .values
        .sumOf { it + 1 }

private fun part2() =
    input
        .getLowPoints()
        .map { findBasinFromLowPoint(it.key, input) }
        .map { it.size }
        .sortedDescending()
        .take(3)
        .reduce { acc, i -> acc * i }

private fun Map<Coordinate, Int>.getLowPoints() =
    filter { (coordinate, height) ->
        listOf(coordinate.above(), coordinate.below(), coordinate.toLeft(), coordinate.toRight())
            .all {
                this.getValue(it) > height
            }
    }

private fun findBasinFromLowPoint(
    startingCoordinate: Coordinate,
    map: Map<Coordinate, Int>
): Set<Coordinate> {
    val visitedCoordinates = mutableSetOf(startingCoordinate)

    fun walk(
        coordinate: Coordinate
    ) {
        if (map.getValue(coordinate) >= 9) return

        visitedCoordinates.add(coordinate)

        val coordinatesToWalk =
            listOf(coordinate.above(), coordinate.below(), coordinate.toLeft(), coordinate.toRight())
        for (coordinateToWalk in coordinatesToWalk) {
            if (coordinateToWalk !in visitedCoordinates) walk(coordinateToWalk)
        }
    }

    walk(startingCoordinate)
    return visitedCoordinates
}

private fun Map<Coordinate, Int>.getValue(coordinate: Coordinate) =
    this[coordinate] ?: Int.MAX_VALUE

private fun Coordinate.above() = Coordinate(x, y - 1)
private fun Coordinate.below() = Coordinate(x, y + 1)
private fun Coordinate.toRight() = Coordinate(x + 1, y)
private fun Coordinate.toLeft() = Coordinate(x - 1, y)
