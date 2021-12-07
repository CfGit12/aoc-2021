package aoc2021

import kotlin.math.abs

private val input = readResourceFileAsIntLine("7.txt")

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() = calculateLowestFuel(Int::linearDistanceFrom)
private fun part2() = calculateLowestFuel(Int::complexDistanceFrom)

private fun calculateLowestFuel(distanceCalculation: Int.(Int) -> Int) =
    input.range()
        .minOf { destination ->
            input.sumOf { it.distanceCalculation(destination) }
        }

private fun List<Int>.range() =
    (this.minOf { it }..this.maxOf { it })

private fun Int.linearDistanceFrom(other: Int) = abs(this - other)
private fun Int.complexDistanceFrom(other: Int) = (0..abs(this - other)).sum()