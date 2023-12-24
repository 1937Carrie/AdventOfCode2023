import java.util.*
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        var startRow: Long = 0
        var startColumn: Long = 0

        val steps: Long = 64
        var answer = 0L

        input.forEachIndexed { index, row ->
            row.forEachIndexed { indexC, ch ->
                if (ch == 'S') {
                    startRow = index.toLong()
                    startColumn = indexC.toLong()
                }
            }
        }

        val dMap = findD(
            startRow,
            startColumn,
            input.map { it.toCharArray() },
            input.size.toLong(),
            input.first().length.toLong()
        )
        val solve = mutableMapOf<Triple<Long, Long, Long>, Long>()

        for (r in input.indices) {
            for (c in input[r].indices) {
                if (QuadrupleD21(0, 0, r.toLong(), c.toLong()) in dMap) {
                    val opt = longArrayOf(-3, -2, -1, 0, 1, 2, 3)
                    for (tr in opt) {
                        for (tc in opt) {
                            if (tr != 0L || tc != 0L) {
                                continue
                            }
                            val d = dMap[QuadrupleD21(tr, tc, r.toLong(), c.toLong())] ?: 0
                            if (d % 2 == steps % 2 && d <= steps) {
                                answer++
                            }
                            if (tr in longArrayOf(opt.first(), opt.last())
                                && tc in longArrayOf(opt.first(), opt.last())
                            ) {
                                answer += solve(d, 2, steps, solve, input.size.toLong())
                            } else if (tr in longArrayOf(opt.first(), opt.last())
                                || tc in longArrayOf(opt.first(), opt.last())
                            ) {
                                answer += solve(d, 1, steps, solve, input.size.toLong())
                            }
                        }
                    }
                }
            }
        }
        return answer
    }

    fun part2(input: List<String>): Long {
        var startRow: Long = 0
        var startColumn: Long = 0

        val steps: Long = 26501365
        var answer = 0L

        input.forEachIndexed { index, row ->
            row.forEachIndexed { indexC, ch ->
                if (ch == 'S') {
                    startRow = index.toLong()
                    startColumn = indexC.toLong()
                }
            }
        }

        val dMap = findD(
            startRow,
            startColumn,
            input.map { it.toCharArray() },
            input.size.toLong(),
            input.first().length.toLong()
        )
        val solve = mutableMapOf<Triple<Long, Long, Long>, Long>()

        for (r in input.indices) {
            for (c in input[r].indices) {
                if (QuadrupleD21(0, 0, r.toLong(), c.toLong()) in dMap) {
                    val opt = longArrayOf(-3, -2, -1, 0, 1, 2, 3)
                    for (tr in opt) {
                        for (tc in opt) {
                            val d = dMap[QuadrupleD21(tr, tc, r.toLong(), c.toLong())] ?: 0
                            if (d % 2 == steps % 2 && d <= steps) {
                                answer++
                            }
                            if (tr in longArrayOf(opt.first(), opt.last())
                                && tc in longArrayOf(opt.first(), opt.last())
                            ) {
                                answer += solve(d, 2, steps, solve, input.size.toLong())
                            } else if (tr in longArrayOf(opt.first(), opt.last())
                                || tc in longArrayOf(opt.first(), opt.last())
                            ) {
                                answer += solve(d, 1, steps, solve, input.size.toLong())
                            }
                        }
                    }
                }
            }
        }
        return answer
    }

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}

fun solve(d: Long, v: Long, steps: Long, solve: MutableMap<Triple<Long, Long, Long>, Long>, rowSize: Long): Long {
    val amt = (steps - d) / rowSize
    val key = Triple(d, v, steps)
    if (solve.containsKey(key)) {
        return solve[key]!!
    }
    var ret: Long = 0
    for (x in 1..amt) {
        if (d + rowSize * x <= steps && (d + rowSize * x) % 2 == (steps % 2)) {
            ret += if (v == 2L) (x + 1) else 1
        }
    }
    solve[key] = ret
    return ret
}


fun findD(
    startRow: Long,
    startColumn: Long,
    grid: List<CharArray>,
    sizeRow: Long,
    sizeColumn: Long
): Map<QuadrupleD21, Long> {
    val dMap = mutableMapOf<QuadrupleD21, Long>()
    val queue = ArrayDeque(listOf(QuintupleD21(0, 0, startRow, startColumn, 0)))

    while (queue.isNotEmpty()) {
        val (tr, tc, r, c, d) = queue.poll()
        var newTr = tr
        var newTc = tc
        var newR = r
        var newC = c
        if (r < 0) {
            newTr -= 1
            newR += sizeRow
        } else if (r >= sizeRow) {
            newTr += 1
            newR -= sizeRow
        }

        if (c < 0) {
            newTc -= 1
            newC += sizeColumn
        } else if (c >= sizeColumn) {
            newTc += 1
            newC -= sizeColumn
        }

        if ((newR in 0..<sizeRow && 0 <= newC && newC < sizeColumn && grid[newR.toInt()][newC.toInt()] != '#').not()) {
            continue
        }

        if (dMap.containsKey(QuadrupleD21(newTr, newTc, newR, newC))) {
            continue
        }

        if (abs(newTr) > 4 || abs(newTc) > 4) {
            continue
        }

        dMap[QuadrupleD21(newTr, newTc, newR, newC)] = d

        for ((dr, dc) in listOf(intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(1, 0), intArrayOf(0, -1))) {
            queue.add(QuintupleD21(newTr, newTc, newR + dr, newC + dc, d + 1))
        }
    }

    return dMap
}

data class QuadrupleD21(val first: Long, val second: Long, val third: Long, val fourth: Long)
data class QuintupleD21(val first: Long, val second: Long, val third: Long, val fourth: Long, val fifth: Long)
