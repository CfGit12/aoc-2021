package aoc2021

private val inputBinary = readResourceFile("16.txt").fromHexToBinary()

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}

private fun part1() {
    println(inputBinary.parsePacket().first.getVersionTotal())
    //println("38006F45291200".fromHexToBinary())
    //println("1010".toInt(2))
    //println("110100101111111000101000".parsePacket())
    //println("8A004A801A8002F478".fromHexToBinary())
    //println("00111000000000000110111101000101001010010001001000000000".parsePacket())
    //println("11101110000000001101010000001100100000100011000001100000".parsePacket())
    //println("8A004A801A8002F478".fromHexToBinary().parsePacket().first.getVersionTotal())
    //println("A0016C880162017C3686B18A3D4780".fromHexToBinary().parsePacket().first.getVersionTotal())

}
private fun part2() = inputBinary.parsePacket().first.getTotalValue()

private fun String.parsePacket(): Pair<Packet, String> {
    val version = this.slice(0..2).toInt(2)
    val typeId = this.slice(3..5).toInt(2)
    if (typeId == 4) {
        val (value, remaining) = this.drop(6).parseLiteral()
        return LiteralPacket(version, typeId, value) to remaining
    } else {
        val lengthTypeId = this[6]
        if (lengthTypeId == '0') {
            val numberOfBits = this.slice(7..21).toInt(2)
            var remaining = this.drop(22)
            var bitsConsumed = 0
            val subPackets = buildList {
                while (bitsConsumed < numberOfBits) {
                    val (packet, remaining2) = remaining.parsePacket()
                    add(packet)
                    bitsConsumed += (remaining.length - remaining2.length)
                    remaining = remaining2
                }
            }
            return OperatorPacket(version, typeId, lengthTypeId, subPackets) to remaining
        } else {
            val numberOfSubPackets = this.slice(7..17).toInt(2)
            var remaining = this.drop(18)
            var subPacketsConsumed = 0
            val subPackets = buildList {
                while (subPacketsConsumed < numberOfSubPackets) {
                    val (packet, remaining2) = remaining.parsePacket()
                    add(packet)
                    subPacketsConsumed++
                    remaining = remaining2
                }
            }
            return OperatorPacket(version, typeId, lengthTypeId, subPackets) to remaining
        }
    }
}

private fun String.parseLiteral(): Pair<Long, String> {
    var remainingString = this
    val decimalString = buildString {
        do {
            val startingBit = remainingString.first()
            append(remainingString.slice(1..4))
            remainingString = remainingString.drop(5)
        } while (startingBit == '1')
    }
    return decimalString.toLong(2) to remainingString
}

private sealed class Packet {
    abstract val version: Int
    abstract val typeId: Int
    abstract fun getVersionTotal(): Int
    abstract fun getTotalValue(): Long
}

private data class LiteralPacket(
    override val version: Int,
    override val typeId: Int,
    val value: Long
) : Packet() {
    override fun getVersionTotal() = version
    override fun getTotalValue() = value
}

private data class OperatorPacket(
    override val version: Int,
    override val typeId: Int,
    val lengthTypeId: Char,
    val subPackets: List<Packet>
) : Packet() {
    override fun getVersionTotal() = version + subPackets.sumOf { it.getVersionTotal() }

    override fun getTotalValue(): Long =
        when (typeId) {
            0 -> subPackets.sumOf { it.getTotalValue() }
            1 -> subPackets.fold(1L) { acc, packet -> acc * packet.getTotalValue()}
            2 -> subPackets.minOf { it.getTotalValue() }
            3 -> subPackets.maxOf { it.getTotalValue() }
            5 -> if (subPackets[0].getTotalValue() > subPackets[1].getTotalValue()) 1 else 0
            6 -> if (subPackets[0].getTotalValue() < subPackets[1].getTotalValue()) 1 else 0
            7 -> if (subPackets[0].getTotalValue() == subPackets[1].getTotalValue()) 1 else 0
            else -> throw RuntimeException("Unrecognised operator")
        }

}

private fun String.fromHexToBinary() =
    this
        .map { it.digitToInt(16).toString(2).padStart(4, '0') }
        .joinToString("")