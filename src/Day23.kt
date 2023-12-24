fun main() {
    fun part1(input: List<String>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
        val visited = Array(input.size) { BooleanArray(input.first().length) }

        return dfsDay23(
            current = start,
            end = end,
            visited = visited,
            getNeighbours = { current ->
                when (input[current.first][current.second]) {
                    '>' -> listOf(current.copy(second = current.second + 1) to 1)
                    '<' -> listOf(current.copy(second = current.second - 1) to 1)
                    'v' -> listOf(current.copy(first = current.first + 1) to 1)
                    '^' -> listOf(current.copy(first = current.first - 1) to 1)
                    else -> {
                        getNeighbours(current, input.map { it.toCharArray() }).map { it to 1 }
                    }
                }
            }
        )
    }

    fun part2(input: List<String>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
        val newForm = input.map { it.toCharArray() }
        val visited = Array(input.size) { BooleanArray(input.first().length) }

        val intersections = mutableMapOf(
            start to mutableListOf<Pair<Pair<Int, Int>, Int>>(),
            end to mutableListOf(),
        )

        for (row in input.indices) {
            for (col in input[row].indices) {
                if (input[row][col] == '.') {
                    val point = Pair(col, row)
                    if (getNeighbours(point, newForm).size > 2) {
                        intersections[point] = mutableListOf()
                    }
                }
            }
        }

        for (junction in intersections.keys) {
            var current = setOf(junction)
            val visited = mutableSetOf(junction)
            var distance = 0

            while (current.isNotEmpty()) {
                distance++
                current = buildSet {
                    for (c in current) {
                        getNeighbours(c, newForm).filter { it !in visited }.forEach { n ->
                            if (n in intersections) {
                                intersections.getValue(junction).add(n to distance)
                            } else {
                                add(n)
                                visited.add(n)
                            }
                        }
                    }
                }
            }
        }

        return dfsDay23(start, end, visited, 0) { current -> intersections.getValue(current) }

    }

    val input = readInput("Day23")
    val start = Pair(0, 1)
    val finish = Pair(input.lastIndex, input.last().lastIndex - 1)

    part1(input, start, finish).println()
    part2(input, start, finish).println() // Gives correct answer on control dataset but wrong on test
}

private fun dfsDay23(
    current: Pair<Int, Int>,
    end: Pair<Int, Int>,
    visited: Array<BooleanArray>,
    distance: Int = 0,
    getNeighbours: (Pair<Int, Int>) -> List<Pair<Pair<Int, Int>, Int>>
): Int {
    if (current == end) return distance

    visited[current.first][current.second] = true
    val neighbours = getNeighbours(current)
    var max = 0

    neighbours.forEach { (neighbour, weight) ->
        if (visited[neighbour.first][neighbour.second]) return@forEach
        val currentLength = dfsDay23(neighbour, end, visited, distance + weight, getNeighbours)
        max = maxOf(max, currentLength)
    }
    visited[current.first][current.second] = false

    return max
}

private fun getNeighbours(current: Pair<Int, Int>, graph: List<CharArray>): List<Pair<Int, Int>> {
    val neighbours = mutableListOf<Pair<Int, Int>>()
    if (current.first - 1 > 0)
        neighbours.add(Pair(current.first - 1, current.second))
    if (current.second + 1 < graph.first().size)
        neighbours.add(Pair(current.first, current.second + 1))
    if (current.first + 1 < graph.size)
        neighbours.add(Pair(current.first + 1, current.second))
    if (current.second - 1 > 0)
        neighbours.add(Pair(current.first, current.second - 1))

    return neighbours.filter { graph[it.first][it.second] != '#' }
}