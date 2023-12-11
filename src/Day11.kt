import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val indexOfRowToExpand = mutableListOf<Int>()
        val indexOfColumnToExpand = mutableListOf<Int>()
        val map = input.map { it.map { it }.toCharArray() }.toMutableList()
        map.forEachIndexed { indexRow, s ->
            if (s.count { it == '.' } == s.size) {
                indexOfRowToExpand.add(indexRow)
            }
        }
        val transposeMatrix = transposeMatrix(map)
        transposeMatrix.forEachIndexed { indexColumn, chars ->
            if (chars.count { it == '.' } == chars.size) {
                indexOfColumnToExpand.add(indexColumn)
            }
        }

        indexOfRowToExpand.forEachIndexed { index, i ->
            map.add(i + index, CharArray(map.first().size) { '.' })
        }
        val transposeMatrix1 = transposeMatrix(map)
        indexOfColumnToExpand.forEachIndexed { index, i ->
            transposeMatrix1.add(i + index, CharArray(transposeMatrix1.first().size) { '.' })
        }
        val mapToWork = transposeMatrix(transposeMatrix1)

        val galaxies = mutableMapOf<Int, Pair<Int, Int>>()
        var counter = 1
        mapToWork.forEachIndexed { index, chars ->
            chars.forEachIndexed { indexChar, c ->
                if (c == '#') {
                    galaxies[counter++] = Pair(index, indexChar)
                }
            }
        }
        var sum = 0
        galaxies.forEach { i, coordinates ->
            for (k in i + 1..galaxies.size) {
                sum += abs(coordinates.first - galaxies[k]!!.first) + abs(coordinates.second - galaxies[k]!!.second)
            }
        }

        return sum
    }

    fun part2(input: List<String>): Long {
        val map = input.map { it.map { it }.toCharArray() }.toMutableList()
        val rowsToExpand = mutableListOf<Int>()
        map.forEachIndexed { index, chars ->
            if (chars.count { it == '.' } == chars.size) rowsToExpand.add(index)
        }
        val columnsToExpand = mutableListOf<Int>()
        transposeMatrix(map).forEachIndexed { index, chars ->
            if (chars.count { it == '.' } == chars.size) columnsToExpand.add(index)
        }

        val galaxies = mutableMapOf<Int, Pair<Int, Int>>()
        var counter = 1
        map.forEachIndexed { index, chars ->
            chars.forEachIndexed { indexChar, c ->
                if (c == '#') {
                    galaxies[counter++] = Pair(index, indexChar)
                }
            }
        }
        val timesLarger = 1_000_000

        val afterExpanded = galaxies.map { (index, coordinate) ->
            val row =
                if (coordinate.first < rowsToExpand.first())
                    coordinate.first
                else
                    (rowsToExpand.indexOf(rowsToExpand.last { it <= coordinate.first }) + 1) * timesLarger + coordinate.first - (rowsToExpand.indexOf(rowsToExpand.last { it <= coordinate.first }) + 1)
            val col =
                if (coordinate.second < columnsToExpand.first())
                    coordinate.second
                else
                    (columnsToExpand.indexOf(columnsToExpand.last { it <= coordinate.second }) + 1) * timesLarger + coordinate.second - (columnsToExpand.indexOf(columnsToExpand.last { it <= coordinate.second }) + 1)
            index to Pair(row, col)
        }.toMap()
        var sum = 0L
        afterExpanded.forEach { (i, coordinates) ->
            for (k in i + 1..afterExpanded.size) {
                sum += abs(coordinates.first - afterExpanded[k]!!.first) + abs(coordinates.second - afterExpanded[k]!!.second)
            }
        }

        return sum
    }

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}

fun transposeMatrix(matrix: MutableList<CharArray>): MutableList<CharArray> {
    val rows = matrix.size
    val columns = matrix[0].size
    val transposedMatrix = Array(columns) { Array(rows) { '0' } }

    for (i in 0 until rows) {
        for (j in 0 until columns) {
            transposedMatrix[j][i] = matrix[i][j]
        }
    }

    return transposedMatrix.map { it.toCharArray() }.toMutableList()
}