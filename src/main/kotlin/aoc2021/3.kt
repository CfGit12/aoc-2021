package aoc2021

private val input = readResourceFileAsLines("3.txt")

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1(): Int {
    val mostCommonBits = input.mostCommonBits()
    val gammaRate = mostCommonBits.fromBinaryToDecimal()
    val epsilonRate = mostCommonBits.inverted().fromBinaryToDecimal()
    return gammaRate * epsilonRate
}

private fun part2(): Int {
    val oxygenRating = findOxygenRating(0, input).toInt(2)
    val co2Rating = findCo2Rating(0, input).toInt(2)
    return oxygenRating * co2Rating
}

private fun findOxygenRating(index: Int, binaryNumbers: List<String>): String {
    if (binaryNumbers.size == 1) return binaryNumbers[0]
    val ones = binaryNumbers.countOf(1, index)
    val zeroes = binaryNumbers.countOf(0, index)
    val mostCommon = if (zeroes > ones) 0 else 1
    val remainingNumbers = binaryNumbers.filter { it[index].digitToInt() == mostCommon }
    return findOxygenRating(index + 1, remainingNumbers)
}

private fun findCo2Rating(index: Int, binaryNumbers: List<String>): String {
    if (binaryNumbers.size == 1) return binaryNumbers[0]
    val ones = binaryNumbers.countOf(1, index)
    val zeroes = binaryNumbers.countOf(0, index)
    val leastCommon = if (zeroes <= ones) 0 else 1
    val remainingNumbers = binaryNumbers.filter { it[index].digitToInt() == leastCommon }
    return findCo2Rating(index + 1, remainingNumbers)
}

private fun List<Int>.fromBinaryToDecimal() =
    joinToString("").toInt(2)

private fun List<Int>.inverted() =
    map { if (it == 1) 0 else 1 }

private fun List<String>.mostCommonBits() =
    (0 until this[0].length).map { this.mostCommonBit(it) }

/**
 * Takes the digit from the specified position of each binary string, then constructs a map to determine
 * the count of how many there are of each, returning the key (digit) of the winning number
 */
private fun List<String>.mostCommonBit(index: Int) =
    this.map { it[index].digitToInt() }.groupBy { it }.maxByOrNull { it.value.size }!!.key

/**
 * Made this for part 2. Could perhaps retrofit part 1 but cba.
 */
private fun List<String>.countOf(digit: Int, index: Int) =
    this.map { it[index].digitToInt() }.count { it == digit }