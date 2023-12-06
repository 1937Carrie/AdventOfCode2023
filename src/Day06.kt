fun main() {
    fun part1(input: List<String>): Int {
        val times = Regex("\\d+").findAll(input[0]).toList().map { it.value.toInt() }
        val distances = Regex("\\d+").findAll(input[1]).toList().map { it.value.toInt() }

        var mulOfRaces = 1
        times.forEachIndexed { index, i ->
            var waysToWin = 0
            for (c in 0..distances[index]) {
                val distance = c * (i - c)
                if (distance > distances[index]) waysToWin++
            }
            mulOfRaces *= waysToWin
        }
        return mulOfRaces
    }

    fun part2(input: List<String>): Int {
        val times = input[0].substringAfter(":").replace(" ", "").toLong()
        val distances = input[1].substringAfter(":").replace(" ", "").toLong()

        var waysToWin = 0
        for (c in 0..times) {
            val distance = c * (times - c)
            if (distance > distances) waysToWin++
        }

        return waysToWin
    }

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}