package aoc2021

typealias Paper = Set<Coordinate>

private val input = readResourceFileAsLines("13.txt")

private val inputCoordinates =
    input
        .filterNot { it.startsWith("fold") || it.isBlank() }
        .map { val (x, y) = it.split(","); Coordinate(x.toInt(), y.toInt()) }
        .toSet()

private val inputInstructions =
    input
        .filter { it.startsWith("fold") }
        .map {
            val (left, right) = it.split("=")
            left.last().toInstruction(right.toInt())
        }

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2:")
    println(part2())
}

private fun part1() =
    inputCoordinates
        .foldPaper(inputInstructions.first())
        .size

private fun part2() =
    inputInstructions.fold(inputCoordinates) { paper, instruction ->
        paper.foldPaper(instruction)
    }.asString()

private fun Paper.foldPaper(instruction: Instruction): Paper {
    val toFoldOver = this.filter { it.getForInstruction(instruction) > instruction.pos }
    val newCoordinates = toFoldOver.map {
        it.getNewFromInstruction(instruction)
    }

    return (this + newCoordinates).filter { it.getForInstruction(instruction) < instruction.pos }.toSet()
}

private fun Paper.asString() =
    buildString {
        for (y in 0..this@asString.maxOf { it.y }) {
            for (x in 0..this@asString.maxOf { it.x }) {
                append(if (this@asString.contains(Coordinate(x, y))) '#' else ' ')
            }
            appendLine()
        }
    }

sealed class Instruction {
    abstract val pos: Int
}

data class FoldLeft(override val pos: Int) : Instruction()
data class FoldUp(override val pos: Int) : Instruction()

private fun Char.toInstruction(pos: Int) =
    when (this) {
        'x' -> FoldLeft(pos)
        'y' -> FoldUp(pos)
        else -> throw RuntimeException()
    }

private fun Coordinate.getForInstruction(instruction: Instruction) =
    if (instruction is FoldUp) y else x

private fun Coordinate.getNewFromInstruction(instruction: Instruction) =
    if (instruction is FoldUp)
        Coordinate(x, instruction.pos - (y - instruction.pos))
    else
        Coordinate(instruction.pos - (x - instruction.pos), y)

