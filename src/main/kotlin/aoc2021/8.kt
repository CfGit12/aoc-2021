package aoc2021

private val input = readResourceFileAsLines("8.txt")
    .map { val (signals, output) = it.split(" | "); signals.split(" ") to output.split(" ") }

fun main() {
    println("abc".toSet())
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() =
    input
        .flatMap { it.second }
        .count { it.length in listOf(2, 3, 4, 7) }

private fun part2() =
    input
        .sumOf { calculateSignalValue(it) }

/**
 * Basic gist of this is there are 7 segments but wired up randomly.
 * So there's 5040 permutations they could be in.
 * Only a single one of them will allow us to map all 10 inputs to a valid segment.
 * Find which it is and construct the outcome from that.
 */
private fun calculateSignalValue(inputRow: Pair<List<String>, List<String>>): Int {
    val signals = inputRow.first
    val output = inputRow.second
    for (permutation in permutations) {
        val permutationMap = permutation.permutationMap()
        val numberOfSegments =
            signals.mapNotNull { signal ->
                segments[signal.unPermute(permutationMap)]
            }.count()
        if (numberOfSegments == 10) {
            return output.map { signal ->
                segments[signal.unPermute(permutationMap)]!!
            }.joinToString("").toInt()
        }
    }
    return 0
}

private fun String.unPermute(map: Map<Char, Char>): Set<Char> =
    this.map { c -> map[c]!! }.toSet()

private val permutations = "abcdefg".permutations()
private fun String.permutationMap() = mapOf(
    this[0] to 'a',
    this[1] to 'b',
    this[2] to 'c',
    this[3] to 'd',
    this[4] to 'e',
    this[5] to 'f',
    this[6] to 'g',
)


/**
 *    aaaa
 *   b    c
 *   b    c
 *    dddd
 *   e    f
 *   e    f
 *    gggg
 */
private val segments = mapOf(
    setOf('a', 'b', 'c', 'e', 'f', 'g') to 0,
    setOf('c', 'f') to 1,
    setOf('a', 'c', 'd', 'e', 'g') to 2,
    setOf('a', 'c', 'd', 'f', 'g') to 3,
    setOf('b', 'c', 'd', 'f') to 4,
    setOf('a', 'b', 'd', 'f', 'g') to 5,
    setOf('a', 'b', 'd', 'e', 'f', 'g') to 6,
    setOf('a', 'c', 'f') to 7,
    setOf('a', 'b', 'c', 'd', 'e', 'f', 'g') to 8,
    setOf('a', 'b', 'c', 'd', 'f', 'g') to 9
)

private fun String.permutations(result: String = ""): List<String> =
    if (isEmpty()) listOf(result) else flatMapIndexed { i, c -> removeRange(i, i + 1).permutations(result + c) }