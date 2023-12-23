fun main() {
    fun part1(input: List<String>): Int {
        val bricks = input.map {
            val coordinateValues = it.replace("~", ",").split(",").map { it.toInt() }
            Brick(
                coordinateValues[0]..coordinateValues[3],
                coordinateValues[1]..coordinateValues[4],
                coordinateValues[2]..coordinateValues[5]
            )
        }.sortedBy { it.z.first }.toMutableList()

        letBreaksFall(bricks)
        val supports = HashMap<Brick, MutableList<Brick>>()
        val supportedBy = HashMap<Brick, MutableList<Brick>>()
        calculateSupportedBricks(bricks, supports, supportedBy)

        var count = 0
        for (brick in bricks) {
            val supportedBricksList = supports[brick]
            if (!supportedBricksList.isNullOrEmpty()) {
                var canRemove = true
                for (b2 in supportedBricksList) {
                    if (supportedBy[b2]?.size == 1) {
                        canRemove = false
                        break
                    }
                }
                if (canRemove) count++
            } else count++
        }
        return count
    }


    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day22")
    part1(input).println()
//    part2(input).println()
}

private fun calculateSupportedBricks(
    bricks: List<Brick>,
    supports: HashMap<Brick, MutableList<Brick>>,
    supportedBy: HashMap<Brick, MutableList<Brick>>
) {
    for (brick in bricks) {
        for (blockBrick in bricks) {
            if (brick === blockBrick) {
                continue
            }
            if (blockBrick.blocks(brick)) {
                supports.computeIfAbsent(blockBrick) { ArrayList() }.add(brick)
                supportedBy.computeIfAbsent(brick) { ArrayList() }.add(blockBrick)
            }
        }
    }
}

private fun letBreaksFall(bricks: MutableList<Brick>) {
    var moved = true
    while (moved) {
        moved = false
        for (i in bricks.indices) {
            if (bricks[i].z.first == 1) continue

            var canBlock = true
            for (blockBrick in bricks) {
                if (blockBrick === bricks[i]) continue

                if (blockBrick.blocks(bricks[i])) {
                    canBlock = false
                    break
                }
            }
            if (canBlock) {
                bricks[i] = bricks[i].copy(z = bricks[i].z.first - 1..<bricks[i].z.last)
                moved = true
            }
        }
    }
}

private data class Brick(var x: IntRange, var y: IntRange, var z: IntRange) {
    fun blocks(moveBrick: Brick): Boolean {
        if (moveBrick.z.first != z.last + 1) {
            return false
        }
        for (x in x) {
            for (y in y) {
                if (x >= moveBrick.x.first && x <= moveBrick.x.last && y >= moveBrick.y.first && y <= moveBrick.y.last) {
                    return true
                }
            }
        }
        return false
    }
}