fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach {
            sum += doJob(it).split(" ").last().toInt()
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        input.forEach {
            sum += doJob2(it).split(" ").first().toInt()
        }
        return sum
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

fun doJob2(str: String): String {
    val split = Regex("-?\\d+").findAll(str).map { it.value }.toMutableList()
    val interpolated = mutableListOf<String>()
    for (i in 1 until split.size) {
        interpolated.add((split[i].toInt() - split[i - 1].toInt()).toString())
    }
    if (interpolated.count { it == "0" } != interpolated.size)
        interpolated.add(0, doJob2(interpolated.joinToString(" ")).split(" ").first())
    (split.add(0, (split.first().toInt() - interpolated.first().toInt()).toString()))
    return split.joinToString(" ")
}

fun doJob(str: String): String {
    val split = str.split(" ").toMutableList()
    val interpolated = mutableListOf<String>()
    for (i in 1 until split.size) {
        interpolated.add((split[i].toInt() - split[i - 1].toInt()).toString())
    }
    if (interpolated.count { it == "0" } != interpolated.size)
        interpolated.add(doJob(interpolated.joinToString(" ")).split(" ").last())
    (split.add((split.last().toInt() + interpolated.last().toInt()).toString()))
    return split.joinToString(" ")
}