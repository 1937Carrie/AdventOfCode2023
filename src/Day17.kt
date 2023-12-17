import java.util.PriorityQueue
// https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm#Using_a_priority_queue
fun main() {
    fun part1(input: List<List<Int>>): Int {
        return run(1, 3, input)
    }

    fun part2(input: List<List<Int>>): Int {
        return run(4, 10, input)
    }

    val input = readInput("Day17")
        .map { string ->
            Regex("\\d")
                .findAll(string)
                .map { result -> result.value.toInt() }
                .toList()
        }
    part1(input).println()
    part2(input).println()
}

fun run(minDist: Int, maxDist: Int, input: List<List<Int>>): Int {
    val queue = PriorityQueue(compareBy<Quadruple<Int>> { it.first })
    queue.add(Quadruple(0, 0, 0, -1))
    val visited = mutableSetOf<Triple<Int, Int, Int>>()
    val weights = mutableMapOf<Triple<Int, Int, Int>, Int>()
    val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))

    while (queue.isNotEmpty()) {
        val (weight, x, y, currentDirectionIndex) = queue.poll()
        if (x == input.size - 1 && y == input.first().size - 1) {
            return weight
        }
        if (Triple(x, y, currentDirectionIndex) in visited) {
            continue
        }
        visited.add(Triple(x, y, currentDirectionIndex))
        directions.forEachIndexed { directionIndex, direction ->
            var weightIncrease = 0
            if (directionIndex == currentDirectionIndex || (directionIndex + 2) % 4 == currentDirectionIndex) {
                return@forEachIndexed
            }
            for (distance in 1..maxDist) {
                val newX = x + direction.first * distance
                val newY = y + direction.second * distance
                if (inr(Pair(newX, newY), input)) {
                    weightIncrease += input[newX][newY]
                    if (distance < minDist) {
                        continue
                    }
                    val newWeight = weight + weightIncrease
                    if (weights.getOrDefault(Triple(newX, newY, directionIndex), Int.MAX_VALUE) <= newWeight) {
                        continue
                    }
                    weights[Triple(newX, newY, directionIndex)] = newWeight
                    queue.add(Quadruple(newWeight, newX, newY, directionIndex))
                }
            }
        }
    }
    return -1
}

fun inr(pos: Pair<Int, Int>, arr: List<List<Int>>): Boolean {
    return pos.first in arr.indices && pos.second in arr.first().indices
}

data class Quadruple<T>(val first: T, val second: T, val third: T, val fourth: T)