package aoc2021

private val part1Input = readResourceFileAsLines("1-1.txt").map { it.toInt() }

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun List<Int>.calculateNumberOfIncreases() =
    zipWithNext { a, b -> if (b > a) 1 else 0 }.sum()

private fun part1() =
    part1Input.calculateNumberOfIncreases()

private fun part2() =
    part1Input
        .windowed(size = 3, step = 1, partialWindows = false)
        .map { it.sum() }
        .calculateNumberOfIncreases()