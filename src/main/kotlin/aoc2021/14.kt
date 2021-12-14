package aoc2021

private val input = readResourceFileAsLines("14.txt")

private val inputTemplate = input.first()

private val inputInstructions = buildMap {
    input
        .takeLastWhile { it.isNotBlank() }
        .map { val (pair, element) = it.split(" -> "); put(pair[0] to pair[1], element.single()) }
}

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() = ageBy(10)
private fun part2() = ageBy(40)

private fun ageBy(amount: Int): Long {
    val pairCounts = inputTemplate.zipWithNext().groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    val updated = (1..amount).fold(pairCounts) { acc, _ ->
        acc.age()
    }
    return updated.countChars()
}

private fun Map<Pair<Char, Char>, Long>.age() =
    buildMap<Pair<Char, Char>, Long> {
        this@age.forEach { (pair, number) ->
            val middleChar = inputInstructions[pair]!!
            put(pair.first to middleChar, getOrDefault(pair.first to middleChar, 0L) + number)
            put(middleChar to pair.second, getOrDefault(middleChar to pair.second, 0L) + number)
        }
    }

/*
    NN -> 4
    NC -> 1
    CN -> 1
    N = 6
    C = 1
    NNNNCNN
    For each 2nd char add amount, and add 1 for very first char
*/
private fun Map<Pair<Char, Char>, Long>.countChars(): Long {
    val frequency = buildMap<Char, Long> {
        var addedFirst = false
        this@countChars.forEach { (pair, amount) ->
            if (!addedFirst) {
                put(pair.first, 1L); addedFirst = true;
            }
            put(pair.second, getOrDefault(pair.second, 0L) + amount)
        }
    }
    return frequency.maxOf { it.value } - frequency.minOf { it.value }
}
