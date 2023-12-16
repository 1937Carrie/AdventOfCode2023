// add to VM options "-Xss1024m"
fun main() {
    fun part1(input: List<String>): Int {
        val modified = input.map { it.toCharArray() }.toTypedArray()
        val visited = Array(modified.size) { CharArray(modified.first().size) { '.' } }
        dfs(Pair(0, 0), visited, Directions.R, modified)
        return visited.sumOf { bArr -> bArr.count { it != '.' } }
    }

    fun part2(input: List<String>): Int {
        val modified = input.map { it.toCharArray() }.toTypedArray()
        val visited = Array(modified.size) { CharArray(modified.first().size) { '.' } }
        var maxEnergizedTiles = 0

        val rows = listOf(0, input.lastIndex)
        rows.forEach { row ->
            val columnIndices = input.first().indices.toMutableList()
            columnIndices.removeFirst()
            columnIndices.removeLast()
            columnIndices.forEach { column ->
                val tmpModified = modified.map { it.clone() }.toTypedArray()
                val tmpVisited = visited.map { it.clone() }.toTypedArray()
                dfs(Pair(row, column), tmpVisited, if (row == 0) Directions.D else Directions.U, tmpModified)
                val tmpSum = tmpVisited.sumOf { chars -> chars.count { it != '.' } }
                maxEnergizedTiles = maxOf(tmpSum, maxEnergizedTiles)
            }
        }
        val columns = listOf(0, input.first().lastIndex)
        columns.forEach { column ->
            val rowIndices = input.indices.toMutableList()
            rowIndices.removeFirst()
            rowIndices.removeLast()
            rowIndices.forEach { row ->
                val tmpModified = modified.map { it.clone() }.toTypedArray()
                val tmpVisited = visited.map { it.clone() }.toTypedArray()
                dfs(Pair(row, column), tmpVisited, if (column == 0) Directions.R else Directions.L, tmpModified)
                val tmpSum = tmpVisited.sumOf { chars -> chars.count { it != '.' } }
                maxEnergizedTiles = maxOf(tmpSum, maxEnergizedTiles)
            }
        }
        val cells = listOf(
            Pair(0, 0),
            Pair(0, input.first().lastIndex),
            Pair(input.lastIndex, 0),
            Pair(input.lastIndex, input.first().lastIndex)
        )
        cells.forEachIndexed { index, (row, column) ->
            val directions = when (index) {
                0 -> listOf(Directions.R, Directions.D)
                1 -> listOf(Directions.L, Directions.D)
                2 -> listOf(Directions.R, Directions.U)
                3 -> listOf(Directions.L, Directions.U)
                else -> error("???")
            }
            directions.forEach { direction ->
                val tmpModified = modified.map { it.clone() }.toTypedArray()
                val tmpVisited = visited.map { it.clone() }.toTypedArray()
                dfs(Pair(row, column), tmpVisited, direction, tmpModified)
                val tmpSum = tmpVisited.sumOf { chars -> chars.count { it != '.' } }
                maxEnergizedTiles = maxOf(tmpSum, maxEnergizedTiles)
            }
        }

        return maxEnergizedTiles
    }

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

fun dfs(position: Pair<Int, Int>, visited: Array<CharArray>, direction: Directions, grid: Array<CharArray>) {
    if (visited[position.first][position.second] == direction.char) return
    visited[position.first][position.second] =
        if (listOf('-', '|', '/', '\\').contains(grid[position.first][position.second]))
            grid[position.first][position.second]
        else {
            if (visited[position.first][position.second] == '.')
                direction.char
            else
                determineChar(visited[position.first][position.second])
        }

    val goNext = mutableListOf<Directions>()
    when (grid[position.first][position.second]) {
        '.' -> goNext.add(direction)
        '\\' -> {
            if (direction == Directions.U) goNext.add(Directions.L)
            if (direction == Directions.R) goNext.add(Directions.D)
            if (direction == Directions.D) goNext.add(Directions.R)
            if (direction == Directions.L) goNext.add(Directions.U)
        }

        '/' -> {
            if (direction == Directions.U) goNext.add(Directions.R)
            if (direction == Directions.R) goNext.add(Directions.U)
            if (direction == Directions.D) goNext.add(Directions.L)
            if (direction == Directions.L) goNext.add(Directions.D)
        }

        '|' -> {
            if (direction == Directions.U || direction == Directions.D) goNext.add(direction)
            else {
                goNext.add(Directions.U)
                goNext.add(Directions.D)
            }
        }

        '-' -> {
            if (direction == Directions.L || direction == Directions.R) goNext.add(direction)
            else {
                goNext.add(Directions.L)
                goNext.add(Directions.R)
            }
        }
    }
    goNext.forEach {
        val nextPosition = when (it) {
            Directions.U -> Pair(position.first - 1, position.second)
            Directions.R -> Pair(position.first, position.second + 1)
            Directions.L -> Pair(position.first, position.second - 1)
            Directions.D -> Pair(position.first + 1, position.second)
        }
        if (nextPosition.first < 0 || nextPosition.first >= grid.size || nextPosition.second < 0 || nextPosition.second >= grid.first().size) return@forEach

        dfs(nextPosition, visited, it, grid)

    }
}

fun determineChar(current: Char): Char {
    if (Directions.entries.map { it.char }.contains(current)) return '2'
    if (current.isDigit() && (2..8).contains(current.digitToInt())) return current + 1
    return current + 1
}

enum class Directions(val char: Char) {
    U('^'), R('>'), L('<'), D('v')
}