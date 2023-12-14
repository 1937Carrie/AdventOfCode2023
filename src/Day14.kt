fun main() {
    fun part1(input: List<String>): Int {
        val input2 = input.map { it.toCharArray() }
        val arrAfter = mutableListOf<CharArray>()
        for (column in 0..<input.first().length) {
            val tmpArray = CharArray(input2.size) { ' ' }
            for (row in input2.indices.reversed()) {
                tmpArray[row] = input2[row][column]
            }
            arrAfter.addAll(listOf(moveRock(tmpArray)))
        }

        return arrAfter.sumOf { column ->
            column.reversed().foldIndexed(0) { index: Int, acc: Int, c: Char ->
                acc + if (c == 'O') index + 1 else 0
            }
        }
    }

    fun part2(input: List<String>): Int {
        val input2 = input.map { it.toCharArray() }
        var arrAfter = mutableListOf<CharArray>()
        repeat(1_000_000_000) {
            arrAfter = moveRock2(input2)
        }

        var rowIndex = arrAfter.size
        return arrAfter.sumOf { row ->
            row.fold(0) { acc: Int, c: Char ->
                acc + if (c == 'O') rowIndex else 0
            }.also { rowIndex-- }
        }
    }

    val input = readInput("Day14_test")
    part1(input).println()
    part2(input).println()
}

fun moveRock2(input: List<CharArray>): MutableList<CharArray> {
    val toTypedArray = input.toTypedArray()
    // North
    for (column in toTypedArray.first().indices) {
        for (row in 0..<toTypedArray.size - 1) {
            if (toTypedArray[row][column] == '#' || toTypedArray[row][column] == 'O') continue
            if (toTypedArray[row][column] == '.') {
                for (k in row + 1..<input.size) {
                    if (toTypedArray[k][column] == '#') break
                    if (toTypedArray[k][column] == 'O') {
                        toTypedArray[row][column] = 'O'
                        toTypedArray[k][column] = '.'
                        break
                    }
                }
            }
        }
    }
    // West
    for (row in toTypedArray.indices) {
        for (column in 0..<toTypedArray.first().size - 1) {
            if (toTypedArray[row][column] == '#' || toTypedArray[row][column] == 'O') continue
            if (toTypedArray[row][column] == '.') {
                for (k in column + 1..<input.first().size) {
                    if (toTypedArray[row][k] == '#') break
                    if (toTypedArray[row][k] == 'O') {
                        toTypedArray[row][column] = 'O'
                        toTypedArray[row][k] = '.'
                        break
                    }
                }
            }
        }
    }
    // South
    for (column in toTypedArray.first().indices) {
        for (row in (1..<toTypedArray.size).reversed()) {
            if (toTypedArray[row][column] == '#' || toTypedArray[row][column] == 'O') continue
            if (toTypedArray[row][column] == '.') {
                for (k in (row - 1 downTo 0)) {
                    if (toTypedArray[k][column] == '#') break
                    if (toTypedArray[k][column] == 'O') {
                        toTypedArray[row][column] = 'O'
                        toTypedArray[k][column] = '.'
                        break
                    }
                }
            }
        }
    }
    // East
    for (row in toTypedArray.indices) {
        for (column in (1..<toTypedArray.first().size).reversed()) {
            if (toTypedArray[row][column] == '#' || toTypedArray[row][column] == 'O') continue
            if (toTypedArray[row][column] == '.') {
                for (k in (0..<column).reversed()) {
                    if (toTypedArray[row][k] == '#') break
                    if (toTypedArray[row][k] == 'O') {
                        toTypedArray[row][column] = 'O'
                        toTypedArray[row][k] = '.'
                        break
                    }
                }
            }
        }
    }
    return toTypedArray.toMutableList()
}

fun moveRock(input: CharArray): CharArray {
    for (i in 0..<input.size - 1) {
        if (input[i] == '#' || input[i] == 'O') continue
        if (input[i] == '.') {
            for (k in i + 1..<input.size) {
                if (input[k] == '#') break
                if (input[k] == 'O') {
                    input[i] = 'O'
                    input[k] = '.'
                    break
                }
            }
        }
    }
    return input
}