package aoc2021

import java.lang.RuntimeException

private typealias OctopusMap = Map<Coordinate, Octopus>

private val input = buildMap {
    readResourceFileAsLines("11.txt")
        .mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                put(Coordinate(x, y), Octopus(c.digitToInt(), false))
            }
        }
}

private data class Octopus(val energy: Int, val hasFlashed: Boolean) {
    fun boostEnergy() = copy(energy = energy + 1)
    fun shouldFlash() = energy > 9 && !hasFlashed
    fun flash() = copy(hasFlashed = true)
    fun reset() = if (hasFlashed) Octopus(0, false) else this
}

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1(): Int {
    var flashCounter = 0
    var octopusMap = input
    repeat(100) {
        octopusMap = octopusMap.boostEnergy()
        var coordinatesToFlash = octopusMap.getCoordinatesToFlash()

        while (coordinatesToFlash.isNotEmpty()) {
            for (coordinateToFlash in coordinatesToFlash) {
                octopusMap = octopusMap.flashAtCoordinate(coordinateToFlash)
                flashCounter++
            }
            coordinatesToFlash = octopusMap.getCoordinatesToFlash()
        }

        octopusMap = octopusMap.resetOctopuses()
    }
    return flashCounter
}

private fun part2(): Int {
    var octopusMap = input

    for (step in 1..Int.MAX_VALUE) {
        octopusMap = octopusMap.boostEnergy()
        var coordinatesToFlash = octopusMap.getCoordinatesToFlash()

        while (coordinatesToFlash.isNotEmpty()) {
            for (coordinateToFlash in coordinatesToFlash) {
                octopusMap = octopusMap.flashAtCoordinate(coordinateToFlash)
            }
            coordinatesToFlash = octopusMap.getCoordinatesToFlash()
        }

        if (octopusMap.all { it.value.hasFlashed }) return step

        octopusMap = octopusMap.resetOctopuses()
    }
    throw RuntimeException("No synced step found")
}

private fun OctopusMap.boostEnergy(): OctopusMap =
    mapValues { it.value.boostEnergy() }

private fun OctopusMap.getCoordinatesToFlash(): Set<Coordinate> =
    filterValues { it.shouldFlash() }
        .keys

private fun OctopusMap.flashAtCoordinate(flashingCoordinate: Coordinate) =
    mapValues { (coordinate, octopus) ->
        when (coordinate) {
            flashingCoordinate -> octopus.flash()
            in flashingCoordinate.getSurroundingCoordinatesIncDiagonals() -> octopus.boostEnergy()
            else -> octopus
        }
    }

private fun OctopusMap.resetOctopuses(): OctopusMap =
    mapValues { it.value.reset() }