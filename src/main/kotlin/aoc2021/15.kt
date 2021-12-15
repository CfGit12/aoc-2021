package aoc2021

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

private typealias CoordinateMap = Map<Coordinate, Int>

private val input = buildMap {
    readResourceFileAsLines("15.txt")
        .mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                put(Coordinate(x, y), c.digitToInt())
            }
        }
}

@OptIn(ExperimentalTime::class)
fun main() {
    val part1Time = measureTime { println("Part 1: ${part1()}") }
    val part2Time = measureTime { println("Part 2: ${part2()}") }
    println("Part 1: $part1Time")
    println("Part 2: $part2Time")
}

private fun part1() = findShortestPath(input)

private fun part2(): Int {
    val maxX = input.bottomRightCoordinate().x
    val maxY = input.bottomRightCoordinate().y
    val largerMap =
        buildMap {
            repeat(5) { x ->
                repeat(5) { y ->
                    input.entries.forEach { (coordinate, value) ->
                        put(
                            Coordinate(coordinate.x + x * (maxX + 1), coordinate.y + y * (maxY + 1)),
                            value.wrapAdd(x + y)
                        )
                    }
                }
            }
        }
    return findShortestPath(largerMap)
}

private fun Int.wrapAdd(amount: Int): Int {
    val added = this + amount
    return if (added < 10) added
    else added + 1 - 10
}

/**
 * Dijkstra with various tweaks to try and keep maps/sets as small as possible as input is 250,000 large.
 */
private fun findShortestPath(coordinateMap: CoordinateMap): Int {
    val smallestDistances = mutableMapOf(
        coordinateMap.topLeftCoordinate() to 0
    )

    val visitedCoordinates = mutableSetOf<Coordinate>()
    val candidateCoordinates = mutableSetOf(coordinateMap.topLeftCoordinate())

    val maxX = coordinateMap.keys.maxOf { it.x }
    val maxY = coordinateMap.keys.maxOf { it.y }
    val bottomRightCoordinate = coordinateMap.bottomRightCoordinate()

    while (true) {
        val coordinate = candidateCoordinates.minByOrNull { smallestDistances.getOrDefault(it, Int.MAX_VALUE) }!!
        val adjacentCoordinates = coordinate
            .getSurroundingCoordinates(minX = 0, minY = 0, maxX = maxX, maxY = maxY)
            .filter { it !in visitedCoordinates }

        for (adjacentCoordinate in adjacentCoordinates) {
            candidateCoordinates.add(adjacentCoordinate)

            val newSmallestDistance = smallestDistances[coordinate]!! + coordinateMap[adjacentCoordinate]!!
            if (newSmallestDistance < smallestDistances.getOrDefault(adjacentCoordinate, Int.MAX_VALUE)) {
                smallestDistances[adjacentCoordinate] = newSmallestDistance
            }
        }

        visitedCoordinates.add(coordinate)
        candidateCoordinates.remove(coordinate)
        if (coordinate == bottomRightCoordinate) break
        smallestDistances.remove(coordinate)
    }

    return smallestDistances[bottomRightCoordinate]!!
}

private fun CoordinateMap.topLeftCoordinate() =
    this.keys.minByOrNull { it.x + it.y }!!

private fun CoordinateMap.bottomRightCoordinate() =
    this.keys.maxByOrNull { it.x + it.y }!!