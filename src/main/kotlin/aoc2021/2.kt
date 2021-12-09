package aoc2021

private val input = readResourceFileAsLines("2.txt")
    .map {
        val split = it.split(" ")
        Direction.fromString(split[0]) to split[1].toInt()
    }

private enum class Direction {
    FORWARD, DOWN, UP;

    companion object {
        fun fromString(value: String) =
            when (value) {
                "forward" -> FORWARD
                "up" -> UP
                "down" -> DOWN
                else -> throw RuntimeException("blarg")
            }
    }
}

private fun Coordinate.forward(distance: Int) =
    copy(x = x + distance)

private fun Coordinate.up(distance: Int) =
    copy(y = y - distance)

private fun Coordinate.down(distance: Int) =
    copy(y = y + distance)

private data class SubPosition(
    val x: Int,
    val y: Int,
    val aim: Int
)

private fun SubPosition.forward(distance: Int) =
    copy(x = x + distance, y = y + distance * aim)

private fun SubPosition.up(distance: Int) =
    copy(aim = aim - distance)

private fun SubPosition.down(distance: Int) =
    copy(aim = aim + distance)

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1(): Int {
    val finalCoordinates = input.fold(Coordinate(0, 0)) { coord, direction ->
        when (direction.first) {
            Direction.FORWARD -> coord.forward(direction.second)
            Direction.UP -> coord.up(direction.second)
            Direction.DOWN -> coord.down(direction.second)
        }
    }
    println("Final coordinates: $finalCoordinates")
    return finalCoordinates.x * finalCoordinates.y
}

private fun part2(): Int {
    val finalCoordinates = input.fold(SubPosition(0, 0, 0)) { position, direction ->
        when (direction.first) {
            Direction.FORWARD -> position.forward(direction.second)
            Direction.UP -> position.up(direction.second)
            Direction.DOWN -> position.down(direction.second)
        }
    }
    println("Final coordinates: $finalCoordinates")
    return finalCoordinates.x * finalCoordinates.y
}