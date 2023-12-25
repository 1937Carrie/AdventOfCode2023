import java.util.LinkedList
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun main() {
    fun part1(input: List<String>): Int {
        val graph = HashMap<String, MutableSet<String>>()
        input.forEach { row ->
            processLine(row, graph)
        }
        val edgeFrequency = HashMap<Set<String>, Int>()
        val vertexes = ArrayList(graph.keys).toList()
        vertexes.indices.forEach { i ->
            val start = vertexes[i]
            for (j in i + 1 until vertexes.size) {
                val target = vertexes[j]
                markEdges(start, target, graph, edgeFrequency)
            }
        }
        edgeFrequency.entries.sortedByDescending { (_, value) -> value }
            .take(3)
            .forEach { (key, _) -> cutEdge(graph, key) }
        val part1Size = findConnectedSize(graph.keys.first(), graph)
        val part2Size = graph.size - part1Size
        return part1Size * part2Size
    }

    val input = readInput("Day25_test")
    part1(input).println()
}

private fun findConnectedSize(start: String, graph: Map<String, MutableSet<String>>): Int {
    val queue = LinkedList<String>()
    val visited = HashSet<String>()
    queue.add(start)
    visited.add(start)
    while (queue.isNotEmpty()) {
        val curr = queue.poll()
        graph[curr]!!.filter { n -> visited.contains(n).not() }
            .forEach { n ->
                queue.add(n)
                visited.add(n)
            }
    }
    return visited.size
}

private fun cutEdge(graph: Map<String, MutableSet<String>>, edge: Set<String>) {
    val it = edge.iterator()
    val a = it.next()
    val b = it.next()
    graph[a]?.remove(b)
    graph[b]?.remove(a)
}

private fun markEdges(
    start: String,
    target: String,
    graph: Map<String, MutableSet<String>>,
    edgeFrequency: MutableMap<Set<String>, Int>
) {
    val queue = LinkedList<StepD25>()
    val visited = HashSet<String>()
    queue.add(StepD25(start, listOf()))
    visited.add(start)
    while (queue.isNotEmpty()) {
        val (vertex, edges) = queue.poll()
        if (target == vertex) {
            edges.forEach { e ->
                val v = edgeFrequency.getOrDefault(e, 0)
                edgeFrequency[e] = v + 1
            }
            return
        }
        graph[vertex]!!.filter { n -> visited.contains(n).not() }
            .forEach { n: String ->
                val nextEdges = ArrayList(edges)
                nextEdges.add(setOf(vertex, n))
                val nextStep = StepD25(n, nextEdges)
                queue.add(nextStep)
                visited.add(n)
            }
    }
}

private fun processLine(line: String, graph: MutableMap<String, MutableSet<String>>) {
    val parts = line.split(Regex(": ")).dropLastWhile { it.isEmpty() }.toTypedArray()
    val name = parts[0]
    val cons = parts[1].split(Regex(" ")).dropLastWhile { it.isEmpty() }.toTypedArray()
    for (con in cons) {
        markConnection(graph, name, con)
    }
}

private fun markConnection(graph: MutableMap<String, MutableSet<String>>, from: String, to: String) {
    graph.computeIfAbsent(from) { HashSet() }.add(to)
    graph.computeIfAbsent(to) { HashSet() }.add(from)
}

data class StepD25(val vertex: String, val edges: List<Set<String>>)