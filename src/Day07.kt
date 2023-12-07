fun main() {
    fun part1(input: List<String>): Int {
        val map = input.map {
            val split = it.split(' ')
            CamelHand(split[0], split[1].toInt())
        }
        val sortedMap: MutableMap<TYPE, List<CamelHand>> = mutableMapOf()
        map.groupBy { it.type }.toSortedMap(compareBy { it.strength }).forEach {
            val sortedBy = it.value.sortedBy {
                it.value.sortByLabel()
            }
            sortedMap[it.key] = sortedBy
        }
        var rank = 1
        var sum = 0
        sortedMap.forEach {
            it.value.forEach {
                sum += it.bid * rank
                rank++
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val map = input.map {
            val split = it.split(' ')
            CamelHand(split[0], split[1].toInt(), true)
        }
        val sortedMap: MutableMap<TYPE, List<CamelHand>> = mutableMapOf()
        map.groupBy { it.type }.toSortedMap(compareBy { it.strength }).forEach {
            val sortedBy = it.value.sortedBy {
                it.value.sortByLabel2()
            }
            sortedMap[it.key] = sortedBy
        }
        var rank = 1
        var sum = 0
        sortedMap.forEach {
            it.value.forEach {
                sum += it.bid * rank
                rank++
            }
        }

        return sum
    }

    val input = readInput("Day07")

    part1(input).println()
    part2(input).println()
}

val labels: Map<String, String> = mapOf(Pair("A", "E"), Pair("K", "D"), Pair("Q", "C"), Pair("J", "B"), Pair("T", "A"))
val labels2: Map<String, String> = mapOf(Pair("A", "E"), Pair("K", "D"), Pair("Q", "C"), Pair("J", "0"), Pair("T", "A"))

fun String.sortByLabel(): String {
    var newString = ""
    this.forEach {
        newString += if (it.isDigit()) it else labels[it.toString()]
    }
    return newString
}

fun String.sortByLabel2(): String {
    var newString = ""
    this.forEach {
        newString += if (it.isDigit()) it else labels2[it.toString()]
    }
    return newString
}

data class CamelHand(val value: String, val bid: Int, val part2: Boolean = false) {
    val type = if (part2.not()) getHandType(value) else getHandTypeP2(value)

    private fun getHandType(value: String): TYPE {
        val associateWith =
            value.chunked(1).distinct().associateWith { ch -> value.count { it == ch[0] } }.toSortedMap()

        return if (associateWith.maxOf { it.value == 5 }) TYPE.FIVE_OF_KIND
        else if (associateWith.maxOf { it.value == 4 }) TYPE.FOUR_OF_KIND
        else if (associateWith.maxOf { it.value } == 3 && associateWith.minOf { it.value } == 2) TYPE.FULL_HOUSE
        else if (associateWith.maxOf { it.value } == 3 && associateWith.minOf { it.value } == 1) TYPE.THREE_OF_KIND
        else if (associateWith.size == 3) TYPE.TWO_PAIR
        else if (associateWith.size == 4) TYPE.ONE_PAIR
        else TYPE.HIGH_CARD
    }

    private fun getHandTypeP2(value: String): TYPE {
        if (value == "JJJJJ") return TYPE.FIVE_OF_KIND
        val associateWith =
            value.chunked(1).distinct().associateWith { ch -> value.count { it == ch[0] } }.toSortedMap()
        var keyMaxValue = ""
        val jValue = associateWith["J"] ?: 0

        val tmp = associateWith.toMutableMap()
        tmp.remove("J")
        tmp.forEach {
            if (it.value == tmp.maxOf { it.value }) keyMaxValue = it.key
        }
        tmp[keyMaxValue] = tmp[keyMaxValue]?.plus(jValue)
        tmp.remove("J")


        return if (tmp.maxOf { it.value == 5 }) TYPE.FIVE_OF_KIND
        else if (tmp.maxOf { it.value == 4 }) TYPE.FOUR_OF_KIND
        else if (tmp.maxOf { it.value } == 3 && tmp.minOf { it.value } == 2) TYPE.FULL_HOUSE
        else if (tmp.maxOf { it.value } == 3 && tmp.minOf { it.value } == 1) TYPE.THREE_OF_KIND
        else if (tmp.size == 3) TYPE.TWO_PAIR
        else if (tmp.size == 4) TYPE.ONE_PAIR
        else TYPE.HIGH_CARD
    }

    override fun toString(): String {
        return "CamelHand(value=$value, bid=$bid, type=$type)"
    }
}

enum class TYPE(val strength: Int) {
    FIVE_OF_KIND(6), FOUR_OF_KIND(5), FULL_HOUSE(4), THREE_OF_KIND(3), TWO_PAIR(2), ONE_PAIR(1), HIGH_CARD(0)
}


