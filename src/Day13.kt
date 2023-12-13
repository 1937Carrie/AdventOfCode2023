fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0

        for (grid in input.joinToString("\n").split("\n\n")) {
            val g = grid.split('\n').map { it.toCharArray() }
            val row = g.size
            val column = g[0].size

            for (indexRow in 0..<row - 1) {
                var notReflection = 0
                for (indexNextRow in 0..<row) {
                    val up = indexRow - indexNextRow
                    val down = indexRow + 1 + indexNextRow
                    if (up in 0..<down && down < row) {
                        for (c in 0..<column) {
                            if (g[up][c] != g[down][c]) notReflection++
                        }
                    }
                }
                if (notReflection == 0) sum += 100 * (indexRow + 1)
            }

            for (indexColumn in 0..<column - 1) {
                var notReflection = 0
                for (indexNextColumn in 0..<column) {
                    val left = indexColumn - indexNextColumn
                    val right = indexColumn + 1 + indexNextColumn
                    if (left in 0..<right && right < column) {
                        for (r in 0..<row) {
                            if (g[r][left] != g[r][right]) notReflection++
                        }
                    }
                }
                if (notReflection == 0) sum += indexColumn + 1
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        for (grid in input.joinToString("\n").split("\n\n")) {
            val g = grid.split('\n').map { it.toCharArray() }
            val row = g.size
            val column = g[0].size

            for (indexColumn in 0..<column - 1) {
                var notReflection = 0
                for (indexNextColumn in 0..<column) {
                    val left = indexColumn - indexNextColumn
                    val right = indexColumn + 1 + indexNextColumn
                    if (left in 0..<right && right < column) {
                        for (r in 0..<row) {
                            if (g[r][left] != g[r][right]) notReflection++
                        }
                    }
                }
                if (notReflection == 1) sum += indexColumn + 1
            }

            for (indexRow in 0..<row - 1) {
                var notReflection = 0
                for (indexNextRow in 0..<row) {
                    val up = indexRow - indexNextRow
                    val down = indexRow + 1 + indexNextRow
                    if (up in 0..<down && down < row) {
                        for (c in 0..<column) {
                            if (g[up][c] != g[down][c]) notReflection++
                        }
                    }
                }
                if (notReflection == 1) sum += 100 * (indexRow + 1)
            }
        }

        return sum
    }

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}