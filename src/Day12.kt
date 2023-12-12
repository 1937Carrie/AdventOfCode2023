fun main() {
    fun part1(input: List<String>): Long {
        return input.sumOf {
            val (conditionRecord, groupSize) = it.split(" ")
            val mem = mutableMapOf<Pair<List<Long>, List<Char>>, Long>()
            getArrangements(conditionRecord.map { it }, groupSize.split(",").map { it.toLong() }, mem)
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val (conditionRecord, groupSize) = line.split(" ")

            val conditionRecordList = mutableListOf<String>()
            val groupSizeList = mutableListOf<Long>()
            repeat(5) {
                conditionRecordList.addAll(listOf(conditionRecord))
                groupSizeList.addAll(groupSize.split(",").map { it.toLong() })
            }
            val joinedConditionRecordList = conditionRecordList.joinToString("?")
            val mem = mutableMapOf<Pair<List<Long>, List<Char>>, Long>()
            getArrangements(joinedConditionRecordList.map { it }, groupSizeList, mem)
        }
    }

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}


fun getArrangements(
    conditionCharList: List<Char>, groupSizeList: List<Long>, mem: MutableMap<Pair<List<Long>, List<Char>>, Long>
): Long {
    if (conditionCharList.isEmpty()) return if (groupSizeList.isEmpty()) 1 else 0

    return when (conditionCharList.first()) {
        '.' -> getArrangements(conditionCharList.drop(1), groupSizeList, mem)
        '#' -> inDamagedCase(conditionCharList, groupSizeList, mem).also {
            mem[groupSizeList to conditionCharList] = it
        }

        '?' -> getArrangements(conditionCharList.drop(1), groupSizeList, mem) + inDamagedCase(
            conditionCharList, groupSizeList, mem
        ).also { mem[groupSizeList to conditionCharList] = it }

        else -> error("Stub")
    }
}

fun inDamagedCase(
    conditionCharList: List<Char>, groupSizeList: List<Long>, mem: MutableMap<Pair<List<Long>, List<Char>>, Long>
): Long {
    mem[groupSizeList to conditionCharList]?.let { return it }

    if (groupSizeList.isEmpty()) return 0

    val x = groupSizeList[0]
    if (conditionCharList.size < x) return 0
    for (i in 0..<x) {
        if (conditionCharList[i.toInt()] == '.') return 0
    }
    if (conditionCharList.size == x.toInt()) return if (groupSizeList.size == 1) 1 else 0
    if (conditionCharList[x.toInt()] == '#') return 0
    return getArrangements(conditionCharList.drop(x.toInt() + 1), groupSizeList.drop(1), mem)
}