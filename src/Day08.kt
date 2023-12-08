fun main() {
    fun part1(input: List<String>): Int {
        val instruction = input.first()

        val map = input.drop(2).associate {
            val key = it.substringBefore(" ")
            val findAll = Regex("\\w+").findAll(it.substringAfter("=")).toList().map { it.value }

            Pair(key, Pair(findAll[0], findAll[1]))
        }

        var steps = 0
        var node = "AAA"
        while (node != "ZZZ") {
            when (instruction[steps % instruction.length]) {
                'L' -> node = map[node]?.first ?: ""
                'R' -> node = map[node]?.second ?: ""
            }
            steps++
        }
        return steps
    }

    fun part2(input: List<String>): Long {
        val instruction = input.first()

        val fullNodeMap = input.drop(2).associate {
            val key = it.substringBefore(" ")
            val findAll = Regex("\\w+").findAll(it.substringAfter("=")).toList().map { it.value }

            Pair(key, Pair(findAll[0], findAll[1]))
        }

        var steps = 0
        var nodeMap = fullNodeMap.filter { it.key[2] == 'A' }.map { it.key }.toMutableList()
        val numberList = mutableListOf<Int>()

        while (nodeMap.isNotEmpty()) {
            val newList = nodeMap.toMutableList()
            var index = 0
            nodeMap.forEach {
                newList.remove(it)
                when (instruction[(steps % instruction.length.toLong()).toInt()]) {
                    'L' -> nodeMap[index] = fullNodeMap[nodeMap[index]]?.first ?: ""
                    'R' -> nodeMap[index] = fullNodeMap[nodeMap[index]]?.second ?: ""
                }
                if (nodeMap[index].endsWith('Z')) {
                    numberList.add(steps + 1)
                } else {
                    newList.add(nodeMap[index])
                }
                index++
            }
            nodeMap = newList
            steps++
        }

        return lcm(numberList.map { it.toLong() }.toLongArray())
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

private fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

private fun lcm(input: LongArray): Long {
    var result = input[0]
    for (i in 1 until input.size) result = lcm(result, input[i])
    return result
}

private fun gcd(a: Long, b: Long): Long {
    var a = a
    var b = b
    while (b > 0) {
        val temp = b
        b = a % b // % is remainder
        a = temp
    }
    return a
}