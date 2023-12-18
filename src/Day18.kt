fun main() {
    fun part1(input: List<String>): Long {
        val direction = { str: String -> str.first() }
        val steps = { str: String -> Regex("\\d+").find(str)!!.value.toInt() }
        return program(input, direction, steps)
    }

    fun part2(input: List<String>): Long {
        val direction = { str: String -> Regex("#([0-9a-f]+)").find(str)!!.groups[1]!!.value.last() }
        val steps = { str: String ->
            Regex("#([0-9a-f]+)").find(str)!!.groups[1]!!.value.dropLast(1).toLong(16).toInt()
        }
        return program(input, direction, steps)
    }

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}

fun program(input: List<String>, direction: (String) -> Char, steps: (String) -> Int): Long {
    var trace = PointDay18Part(0, 0)
    var result: Long = 0
    var lines = 0
    var offset = 0.0
    input.forEach {
        val direction = direction(it)
        val steps = steps(it)
        val next = trace.move(direction, steps)
        result += trace.col.toLong() * next.row
        result -= trace.row.toLong() * next.col
        trace = next
        offset += (steps - 1) / 2.0
        lines++
    }
    lines /= 2
    offset += (lines + 2) * 0.75 + (lines - 2) * 0.25
    if (result < 0) result *= -1
    result /= 2
    result = (result + offset).toLong()
    return result
}

data class PointDay18Part(var row: Int, var col: Int) {
    fun move(dir: Char, steps: Int): PointDay18Part {
        val p = PointDay18Part(row, col)
        when (dir) {
            '0' -> p.col += steps
            '1' -> p.row += steps
            '2' -> p.col -= steps
            '3' -> p.row -= steps
            'R' -> p.col += steps
            'L' -> p.col -= steps
            'U' -> p.row -= steps
            'D' -> p.row += steps
        }
        return p
    }
}