fun main() {
    fun part1(input: List<String>): Int {
        var startPosition = Pair(0, 0)

        val possibleWays = mutableListOf<Pair<Int, Int>>()
        input.forEachIndexed { index, s ->
            if (s.contains("S")) {
                startPosition = Pair(index, s.indexOf("S"))
                if ((startPosition.first - 1) >= 0 &&
                    canGoNext(Pair(startPosition.first - 1, startPosition.second), input, startPosition) != null
                ) possibleWays.add(Pair(startPosition.first - 1, startPosition.second))
                if ((startPosition.first + 1) < input.size &&
                    canGoNext(Pair(startPosition.first + 1, startPosition.second), input, startPosition) != null
                ) possibleWays.add(Pair(startPosition.first + 1, startPosition.second))
                if ((startPosition.second - 1) >= 0 &&
                    canGoNext(Pair(startPosition.first, startPosition.second - 1), input, startPosition) != null
                )
                    possibleWays.add(Pair(startPosition.first, startPosition.second - 1))
                if ((startPosition.second - 1) < input.first().length &&
                    canGoNext(Pair(startPosition.first, startPosition.second + 1), input, startPosition) != null
                )
                    possibleWays.add(Pair(startPosition.first, startPosition.second + 1))
                return@forEachIndexed
            }
        }
        var sum = 0
        val visitedArr = Array(input.size) { BooleanArray(input.first().length) { false } }
        val distanceArr = Array(input.size) { IntArray(input.first().length) { -1 } }
        visitedArr[startPosition.first][startPosition.second] = true
        distanceArr[startPosition.first][startPosition.second] = 0

        possibleWays.forEach {
            var localSum = 0
            var previousPoint = it
            var vector = canGoNext2(it, input, canGoNext(it, input, startPosition)!!)
            visitedArr[previousPoint.first][previousPoint.second] = true
            distanceArr[previousPoint.first][previousPoint.second] = 1
            while (vector != null) {
                val nextPoint = when (vector) {
                    CARDINALDIRECTION.N -> Pair(previousPoint.first - 1, previousPoint.second)
                    CARDINALDIRECTION.E -> Pair(previousPoint.first, previousPoint.second + 1)
                    CARDINALDIRECTION.S -> Pair(previousPoint.first + 1, previousPoint.second)
                    CARDINALDIRECTION.W -> Pair(previousPoint.first, previousPoint.second - 1)
                }
                localSum++

                if (localSum < distanceArr[previousPoint.first][previousPoint.second] || distanceArr[previousPoint.first][previousPoint.second] == -1) {
                    distanceArr[previousPoint.first][previousPoint.second] = localSum
                }
                if (visitedArr[nextPoint.first][nextPoint.second]) return@forEach
                vector = canGoNext2(nextPoint, input, vector)

                previousPoint = nextPoint
            }
            sum = if (sum < localSum) localSum else sum
        }

        return distanceArr.maxOf { it.maxOf { it } }
    }

    fun part2(inputRaw: List<String>): Int {
        var startPosition = Pair(0, 0)

        val possibleWays = mutableListOf<Pair<Pair<Int, Int>, CARDINALDIRECTION>>()
        inputRaw.forEachIndexed { index, s ->
            if (s.contains("S")) {
                startPosition = Pair(index, s.indexOf("S"))
                if ((startPosition.first - 1) >= 0 &&
                    canGoNext(Pair(startPosition.first - 1, startPosition.second), inputRaw, startPosition) != null
                ) possibleWays.add(
                    Pair(
                        Pair(startPosition.first - 1, startPosition.second),
                        canGoNext(Pair(startPosition.first - 1, startPosition.second), inputRaw, startPosition)!!
                    )
                )
                if ((startPosition.first + 1) < inputRaw.size &&
                    canGoNext(Pair(startPosition.first + 1, startPosition.second), inputRaw, startPosition) != null
                ) possibleWays.add(
                    Pair(
                        Pair(startPosition.first + 1, startPosition.second),
                        canGoNext(Pair(startPosition.first + 1, startPosition.second), inputRaw, startPosition)!!
                    )
                )
                if ((startPosition.second - 1) >= 0 &&
                    canGoNext(Pair(startPosition.first, startPosition.second - 1), inputRaw, startPosition) != null
                ) possibleWays.add(
                    Pair(
                        Pair(startPosition.first, startPosition.second - 1),
                        canGoNext(Pair(startPosition.first, startPosition.second - 1), inputRaw, startPosition)!!
                    )
                )
                if ((startPosition.second - 1) < inputRaw.first().length &&
                    canGoNext(Pair(startPosition.first, startPosition.second + 1), inputRaw, startPosition) != null
                ) possibleWays.add(
                    Pair(
                        Pair(startPosition.first, startPosition.second + 1),
                        canGoNext(Pair(startPosition.first, startPosition.second + 1), inputRaw, startPosition)!!
                    )
                )
                return@forEachIndexed
            }
        }
        val map = possibleWays.map { it.second }
        val input = inputRaw.map { it }.toMutableList()
        val replacedWith = when {
            map.contains(CARDINALDIRECTION.N) && map.contains(CARDINALDIRECTION.W) -> "J"
            map.contains(CARDINALDIRECTION.N) && map.contains(CARDINALDIRECTION.E) -> "L"
            map.contains(CARDINALDIRECTION.S) && map.contains(CARDINALDIRECTION.W) -> "7"
            map.contains(CARDINALDIRECTION.S) && map.contains(CARDINALDIRECTION.E) -> "F"
            map.contains(CARDINALDIRECTION.N) && map.contains(CARDINALDIRECTION.S) -> "|"
            map.contains(CARDINALDIRECTION.W) && map.contains(CARDINALDIRECTION.E) -> "-"
            else -> "S"
        }
        input[startPosition.first] = input[startPosition.first].replace("S", replacedWith)
        var sum = 0
        val visitedArr = Array(input.size) { BooleanArray(input.first().length) { false } }
        val distanceArr = Array(input.size) { IntArray(input.first().length) { -1 } }
        visitedArr[startPosition.first][startPosition.second] = true
        distanceArr[startPosition.first][startPosition.second] = 0

        possibleWays.forEach { (coordinate, direction) ->
            var localSum = 0
            var previousPoint = coordinate
            var vector = canGoNext2(coordinate, input, canGoNext(coordinate, input, startPosition)!!)
            visitedArr[previousPoint.first][previousPoint.second] = true
            distanceArr[previousPoint.first][previousPoint.second] = 1
            while (vector != null) {
                val nextPoint = when (vector) {
                    CARDINALDIRECTION.N -> Pair(previousPoint.first - 1, previousPoint.second)
                    CARDINALDIRECTION.E -> Pair(previousPoint.first, previousPoint.second + 1)
                    CARDINALDIRECTION.S -> Pair(previousPoint.first + 1, previousPoint.second)
                    CARDINALDIRECTION.W -> Pair(previousPoint.first, previousPoint.second - 1)
                    null -> Pair(0, 0)
                }
                localSum++

                if (localSum < distanceArr[previousPoint.first][previousPoint.second] || distanceArr[previousPoint.first][previousPoint.second] == -1) {
                    distanceArr[previousPoint.first][previousPoint.second] = localSum
                }
                if (visitedArr[nextPoint.first][nextPoint.second]) return@forEach
                vector = canGoNext2(nextPoint, input, vector!!)

                previousPoint = nextPoint
            }
            sum = if (sum < localSum) localSum else sum
        }
        val visualisedArr = Array(input.size) { Array(input.first().length) { "" } }
        distanceArr.forEachIndexed { indexR, ints ->
            ints.forEachIndexed { indexC, i ->
                visualisedArr[indexR][indexC] = if (i == -1) (-1).toString() else input[indexR][indexC].toString()
            }
        }
        var sumOfInners = 0
        input.forEachIndexed row@{ indexR, s ->
            var inPipe = 0
            var pipeWalls = 0
            var lastC = ' '
            s.forEachIndexed column@{ indexC, c ->
                if (distanceArr[indexR][indexC] == -1) {
                    if (pipeWalls != 0 && pipeWalls % 2 != 0) inPipe++
                } else {
                    if (c != '-') {
                        if (lastC == 'F' && c == 'J' || lastC == 'L' && c == '7')
                            pipeWalls--
                        lastC = c
                        pipeWalls++
                    }
                }
            }
            sumOfInners += inPipe
        }

        return sumOfInners
    }

    val input = readInput("Day10")

    part1(input).println()
    part2(input).println()
}


fun canGoNext2(
    position: Pair<Int, Int>,
    input: List<String>,
    previousVector: CARDINALDIRECTION
): CARDINALDIRECTION? {
    val currentChar = input[position.first][position.second]
    when (previousVector) {
        CARDINALDIRECTION.N -> {
            when (currentChar) {
                '|' -> if (position.first - 1 >= 0) return CARDINALDIRECTION.N
                '7' -> if (position.second - 1 >= 0) return CARDINALDIRECTION.W
                'F' -> if (position.second + 1 < input.first().length) return CARDINALDIRECTION.E
            }
        }

        CARDINALDIRECTION.E -> {
            when (currentChar) {
                '-' -> if (position.second + 1 < input.first().length) return CARDINALDIRECTION.E
                '7' -> if (position.first + 1 < input.size) return CARDINALDIRECTION.S
                'J' -> if (position.first - 1 >= 0) return CARDINALDIRECTION.N
            }
        }

        CARDINALDIRECTION.S -> {
            when (currentChar) {
                '|' -> if (position.first + 1 < input.size) return CARDINALDIRECTION.S
                'J' -> if (position.second - 1 >= 0) return CARDINALDIRECTION.W
                'L' -> if (position.second + 1 < input.first().length) return CARDINALDIRECTION.E
            }
        }

        CARDINALDIRECTION.W -> {
            when (currentChar) {
                '-' -> if (position.second - 1 >= 0) return CARDINALDIRECTION.W
                'L' -> if (position.first - 1 >= 0) return CARDINALDIRECTION.N
                'F' -> if (position.first + 1 < input.first().length) return CARDINALDIRECTION.S
            }
        }
    }
    return null
}

fun canGoNext(position: Pair<Int, Int>, input: List<String>, previousPoint: Pair<Int, Int>): CARDINALDIRECTION? {
    if (Pair(position.first + 1, position.second) == previousPoint) {
        if (CARDINALDIRECTION.N.validTiles.contains(input[position.first][position.second])) return CARDINALDIRECTION.N
    }
    if (Pair(position.first - 1, position.second) == previousPoint) {
        if (CARDINALDIRECTION.S.validTiles.contains(input[position.first][position.second])) return CARDINALDIRECTION.S
    }
    if (Pair(position.first, position.second + 1) == previousPoint) {
        if (CARDINALDIRECTION.W.validTiles.contains(input[position.first][position.second])) return CARDINALDIRECTION.W
    }
    if (Pair(position.first, position.second - 1) == previousPoint) {
        if (CARDINALDIRECTION.E.validTiles.contains(input[position.first][position.second])) return CARDINALDIRECTION.E
    }
    return null
}

enum class CARDINALDIRECTION(val validTiles: List<Char>) {
    N(listOf('|', '7', 'F')), E(listOf('-', 'J', '7')), S(listOf('|', 'L', 'J')), W(listOf('-', 'L', 'F'))
}