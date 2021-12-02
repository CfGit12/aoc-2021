package aoc2021

private val input = readResourceFileAsLines("1.txt").map { it.toInt() }

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun List<Int>.calculateNumberOfIncreases() =
    zipWithNext { a, b -> if (b > a) 1 else 0 }.sum()

private fun part1() =
    input.calculateNumberOfIncreases()

private fun part2() =
    input
        .windowed(size = 3, step = 1, partialWindows = false)
        .map { it.sum() }
        .calculateNumberOfIncreases()