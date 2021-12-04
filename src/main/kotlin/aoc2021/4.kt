package aoc2021

private val input = readResourceFileAsLines("4.txt")
private val numbers = input[0].split(",").toList().map { it.toInt() }
private val initialBoards = input
    .subList(2, input.size)
    .filter { it.isNotBlank() }
    .windowed(size = 5, step = 5)
    .map { listOfFiveNumberStrings ->
        listOfFiveNumberStrings.map { stringOfFiveNumbers ->
            stringOfFiveNumbers.split(" ").filter { it.isNotBlank() }.map { BingoNumber.fromInt(it.toInt()) }
        }
    }
    .map {
        BingoBoard.fromBingoNumberRows(it)
    }

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() =
    playBingo().minByOrNull { it.winningTurn ?: Int.MAX_VALUE }!!.winningScore!!

private fun part2() =
    playBingo().maxByOrNull { it.winningTurn ?: Int.MIN_VALUE }!!.winningScore!!

private fun playBingo(): List<BingoBoard> =
    numbers.foldIndexed(initialBoards) { index, boards, number ->
        boards.map { it.markNumber(number, index) }
    }

private data class BingoNumber private constructor(
    val number: Int,
    val marked: Boolean
) {
    fun mark() = copy(marked = true)

    companion object {
        fun fromInt(number: Int) = BingoNumber(number, marked = false)
    }
}

private data class BingoBoard private constructor(
    private val rows: List<List<BingoNumber>>,
    val winningScore: Int?,
    val winningTurn: Int?
) {

    fun markNumber(number: Int, turn: Int): BingoBoard {
        // Don't mark an already winning board
        if (winningScore != null) return this

        val updatedRows = rows.map { row ->
            row.map { bingoNumber -> if (bingoNumber.number == number) bingoNumber.mark() else bingoNumber }
        }

        val updatedColumns = updatedRows.indices.map { index -> updatedRows.map { it[index] } }

        val isWinner =
            updatedRows.any { row -> row.all { it.marked } } ||
                    updatedColumns.any { column -> column.all { it.marked } }

        val winningScore = if (isWinner) {
            val unmarkedTotal = updatedRows.sumOf { row ->
                row.sumOf { bingoNumber -> if (!bingoNumber.marked) bingoNumber.number else 0 }
            }
            unmarkedTotal * number
        } else null

        val winningTurn = if (isWinner) turn else null

        return BingoBoard(updatedRows, winningScore, winningTurn)
    }

    companion object {
        fun fromBingoNumberRows(bingoNumberRows: List<List<BingoNumber>>) =
            BingoBoard(
                rows = bingoNumberRows,
                winningScore = null,
                winningTurn = null,
            )
    }
}
