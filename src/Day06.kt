fun main() {
    fun part1(input: List<String>): Long {
        val parsed = input.map {
            it.substringAfter(" ").trimStart().split(Regex("\\s+"))
        }
        val transformed = parsed[0].zip(parsed[1]) { a, b ->
            Pair(a.toLong(), b.toLong())
        }

        var mulOfRaces = 1L
        transformed.forEach {
            var waysToLose = 0

            for (c in 0..it.first) {
                val distance = c * (it.first - c)
                if (distance <= it.second) waysToLose++
                else break
            }
            for (c in (0..it.first).reversed()) {
                val distance = c * (it.first - c)
                if (distance <= it.second) waysToLose++
                else break
            }

            mulOfRaces *= (it.first + 1) - waysToLose
        }
        return mulOfRaces
    }

    fun part2(input: List<String>): Long {
        val times = input[0].substringAfter(":").replace(" ", "").toLong()
        val distances = input[1].substringAfter(":").replace(" ", "").toLong()

        var waysToLose = 0L
        for (c in 0..times) {
            val distance = c * (times - c)
            if (distance <= distances) waysToLose++
            else break
        }
        for (c in (0..times).reversed()) {
            val distance = c * (times - c)
            if (distance <= distances) waysToLose++
            else break
        }

        return (times + 1) - waysToLose
    }

    val input = readInput("Day06")

    part1(input).println()
    part2(input).println()
}