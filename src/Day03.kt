//Based on https://topaz.github.io/paste/#XQAAAQAcAgAAAAAAAAA0m0pnuFI8c+fPp4G3Y5M2miSs3R6AnrKm3fbDkugpdVsCgQOTZL/yzxGwy/N08UeBslxE7G36XluuSq4Y/2FE0+4nPcdj9lpkrrjBk5HRCFLEKuPjUV8tYPx04VDoJ1c6yyLzScmAGwNvzpPoqb5PkRyyy4dSEcuEDe/k0/U7h7pZVh4eTrNAIPsTNZohcltxuwuA4lrZSN37i0QZiufFpvLVyhV/dLBnmSr+2jwFcFE+W6OEIFQxK6MIJ2z7TWKj8lg6yV4yhJzTm+c+QHh2omzhGVLd2WdcHdhjmCyC+Btbr3yCqemYb/6tMUvz8VchnyHstx7QKKeLVmTOEyYqHH/qRDhlKXSQ23RWuPibCf4quQUPGpPDRsH4KITzLbIUVUdssnSp6ffcHO+dAISdzBOiznl5/+PI+jE=
fun main() {

    fun part1(input: List<String>): Int {
        return commonPart(input).values.sumOf { it.sum() }
    }

    fun part2(input: List<String>): Int {
        return commonPart(input).values.filter { it.size == 2 }.sumOf { it.reduce(Int::times) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

fun commonPart(input: List<String>): MutableMap<Pair<Int, Int>, MutableList<Int>> {
    val chars = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()

    for (r in input.indices) {
        for (c in 0 until input[r].length) {
            if (input[r][c] !in "01234566789.") {
                chars[Pair(r, c)] = mutableListOf()
            }
        }
    }

    for ((indexRow, row) in input.withIndex()) {
        val matcher = Regex("\\d+").toPattern().matcher(row)

        while (matcher.find()) {
            val edge = mutableSetOf<Pair<Int, Int>>()
            for (i in (indexRow - 1)..(indexRow + 1)) {
                for (j in (matcher.start() - 1)..(matcher.end())) {
                    edge.add(Pair(i, j))
                }
            }

            for (o in edge.intersect(chars.keys)) {
                chars[o]?.add(matcher.group()!!.toInt())
            }
        }
    }

    return chars
}