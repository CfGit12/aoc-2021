package aoc2021

import kotlin.math.abs
import kotlin.math.floor

private val input = readResourceFileAsLines("5.txt")
    .map {
        val (p1, p2) = it.split(" -> ")
        fun buildPoint(string: String): Point {
            val (x, y) = string.split(",")
            return Point(x.toInt(), y.toInt())
        }
        Line(buildPoint(p1), buildPoint(p2))
    }

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() =
    input.calculateIntersections { it.isHorizontal || it.isVertical }

private fun part2() =
    input.calculateIntersections { it.isHorizontal || it.isVertical || it.isDiagonal }

private fun List<Line>.calculateIntersections(predicate: (Line) -> Boolean) =
    this.filter(predicate)
        .flatMap { it.allPoints }
        .groupingBy { it }.eachCount()
        .count { it.value >= 2 }


private data class Point(
    val x: Int,
    val y: Int
)

private data class Line(
    val from: Point,
    val to: Point
) {
    val isVertical = from.x == to.x
    val isHorizontal = from.y == to.y

    val gradient: Double? =
        if (isVertical) null
        else (from.y - to.y).toDouble() / (from.x - to.x)

    val intercept: Double =
        if (gradient == null) from.x.toDouble()
        else from.y - gradient * from.x

    val isDiagonal = if (gradient == null) false else (abs(gradient) == 1.0)

    val allPoints: List<Point> =
        if (isVertical) {
            (from.y toward to.y).map { y -> Point(from.x, y) }
        } else {
            (from.x toward to.x).mapNotNull { x ->
                val y = gradient!! * x + intercept
                if (y.isInt()) Point(x, y.toInt()) else null
            }
        }
}

private fun Double.isInt() = floor(this) == this

private infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}