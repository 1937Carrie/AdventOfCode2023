import java.util.regex.Pattern

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach outer@{ s ->
            val matcher = Pattern.compile("Game (\\d+):").matcher(s)
            matcher.find()
            val lineNumber = matcher.group(1).toInt()

            Regex("\\d+ red").findAll(s).forEach {
                if (Regex("\\d+").find(it.value)!!.value.toInt() > 12) return@outer
            }
            Regex("\\d+ green").findAll(s).forEach {
                if (Regex("\\d+").find(it.value)!!.value.toInt() > 13) return@outer
            }

            Regex("\\d+ blue").findAll(s).forEach {
                if (Regex("\\d+").find(it.value)!!.value.toInt() > 14) return@outer
            }

            sum += lineNumber
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val red = Regex("\\d+ red").findAll(it).toList().maxOf {
                Regex("\\d+").find(it.value)!!.value.toInt()
            }
            val green = Regex("\\d+ green").findAll(it).toList().maxOf {
                Regex("\\d+").find(it.value)!!.value.toInt()
            }
            val blue = Regex("\\d+ blue").findAll(it).toList().maxOf {
                Regex("\\d+").find(it.value)!!.value.toInt()
            }
            red * green * blue
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}