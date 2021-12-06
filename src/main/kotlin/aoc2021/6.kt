package aoc2021

private val input = readResourceFileAsLines("6.txt")
    .first()
    .split(",")
    .map { it.toInt() }

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() = calculateAgeAfter(80)

private fun part2() = calculateAgeAfter(256)

private fun calculateAgeAfter(days: Int): Long {
    val ageMap = (-1..8).associateBy({ it }, { 0L }).toMutableMap()
    for (age in input) {
        ageMap[age] = ageMap[age]!! + 1
    }
    repeat(days) {
        ageMap.age()
    }
    return ageMap.values.sum()
}

private fun MutableMap<Int, Long>.age(){
    // Shift all the values down
    this[-1] = this[0]!!
    this[0] = this[1]!!
    this[1] = this[2]!!
    this[2] = this[3]!!
    this[3] = this[4]!!
    this[4] = this[5]!!
    this[5] = this[6]!!
    this[6] = this[7]!!
    this[7] = this[8]!!
    // Give birth to shiny new lanternfish
    this[8] = this[-1]!!
    // Start birth cycle again
    this[6] = this[6]!! + this[-1]!!
    // Re-init to 0
    this[-1] = 0
}
