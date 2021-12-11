package aoc2021

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
    fun flash() = copy(hasFlashed = true)
    fun reset() = Octopus(0, false)
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
        octopusMap = octopusMap.mapValues { (_, octopus) ->
            if (octopus.hasFlashed) octopus.reset() else octopus
        }
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
        if (octopusMap.all { (_, octopus) -> octopus.hasFlashed }) return step
        octopusMap = octopusMap.mapValues { (_, octopus) ->
            if (octopus.hasFlashed) octopus.reset() else octopus
        }
    }
    return -1
}

private fun OctopusMap.boostEnergy(): OctopusMap =
    mapValues { it.value.copy(energy = it.value.energy + 1) }

private fun OctopusMap.getCoordinatesToFlash(): Set<Coordinate> =
    filterValues { it.energy > 9 && !it.hasFlashed }
        .keys

private fun OctopusMap.flashAtCoordinate(flashingCoordinate: Coordinate) =
    mapValues { (coordinate, octopus) ->
        when (coordinate) {
            flashingCoordinate -> octopus.flash()
            in flashingCoordinate.getSurroundingCoordinatesIncDiagonals() -> octopus.boostEnergy()
            else -> octopus
        }
    }
