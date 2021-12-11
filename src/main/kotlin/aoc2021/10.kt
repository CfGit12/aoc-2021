package aoc2021

private val input = readResourceFileAsLines("10.txt")

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() =
    input
        .map { it.validateChunk() }
        .filterIsInstance<Failure>()
        .sumOf { it.failedChar.part1Score() }

private fun part2() =
    input
        .map { it.validateChunk() }
        .filterIsInstance<Incomplete>()
        .map { it.remainingStack.toList().reversed().calculatePart2Score() }
        .sorted()
        .middleValue()

private fun List<Char>.calculatePart2Score(): Long =
    map { it.matchingSymbol() }
        .fold(0L) { score, symbol ->
            score * 5 + symbol.part2Score()
        }

private sealed class ValidationResult
private object Ok : ValidationResult()
private data class Incomplete(val remainingStack: Stack<Char>) : ValidationResult()
private data class Failure(val failedChar: Char) : ValidationResult()

private fun String.validateChunk(): ValidationResult {
    val stack: Stack<Char> = mutableListOf()
    for (symbol in this) {
        if (symbol.isOpeningSymbol()) {
            stack.push(symbol)
        } else  {
            val stackSymbol = stack.pop()
            if (stackSymbol?.matchingSymbol() != symbol) {
                return Failure(symbol)
            }
        }
    }
    return if (stack.isEmpty()) Ok else Incomplete(stack)
}

private fun Char.isOpeningSymbol() = this in listOf('(', '[', '{', '<')

private fun Char.matchingSymbol() =
    when (this) {
        '(' -> ')'
        '[' -> ']'
        '{' -> '}'
        '<' -> '>'
        else -> throw RuntimeException()
    }

private fun Char.part1Score() =
    when (this) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw RuntimeException()
    }

val pairs = ("([{<" zip ")]}>").toMap()

private fun Char.part2Score() =
    when (this) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> throw RuntimeException()
    }

private fun <T> List<T>.middleValue(): T =
    this[size / 2]