fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            (Regex("\\d").find(it)?.value + Regex("\\d").find(it.reversed())?.value).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val replacedBegin = replace(it)
            val replacedEnd = replace(it.reversed(), true)

            (Regex("\\d").find(replacedBegin)?.value + Regex("\\d").find(replacedEnd)?.value).toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_1_test")
    val testInput2 = readInput("Day01_2_test")
    check(part1(testInput) == 142)
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

val digits = listOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
)

fun replace(input: String, reversed: Boolean = false): String {
    val regex = if (reversed) Regex("eno|owt|eerht|ruof|evif|xis|neves|thgie|enin")
    else Regex("one|two|three|four|five|six|seven|eight|nine")
    val oldValue = regex.find(input)?.value ?: ""
    if (oldValue == "") return input
    val newValue = digits.find { (if (reversed) it.first.reversed() else it.first) == oldValue }
    return replace(input.replace(oldValue, newValue?.second.toString(), reversed))
}
