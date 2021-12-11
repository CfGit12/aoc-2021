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
        .map { (coordinate, _) -> findBasinFromLowPoint(coordinate) }
        .map { it.size }
        .sortedDescending()
        .take(3)
        .reduce { acc, i -> acc * i }

private fun Map<Coordinate, Int>.getLowPoints() =
    filter { (coordinate, height) ->
        coordinate
            .getSurroundingCoordinates()
            .all {
                this.getHeight(it) > height
            }
    }

private fun findBasinFromLowPoint(
    startingCoordinate: Coordinate
): Set<Coordinate> {
    val visitedCoordinates = mutableSetOf<Coordinate>()

    /**
     * Recursively walk in all 4 directions, keeping a set of visited coordinates to a) prevent repeat visits, and
     * b) return. Recursion base case is reaching a coordinate of height 9 or greater (greater being edge of map).
     */
    fun walk(
        coordinate: Coordinate
    ) {
        if (input.getHeight(coordinate) >= 9) return

        visitedCoordinates.add(coordinate)

        for (coordinateToWalk in coordinate.getSurroundingCoordinates()) {
            if (coordinateToWalk !in visitedCoordinates) walk(coordinateToWalk)
        }
    }

    walk(startingCoordinate)
    return visitedCoordinates
}

private fun Map<Coordinate, Int>.getHeight(coordinate: Coordinate) =
    this[coordinate] ?: Int.MAX_VALUE
