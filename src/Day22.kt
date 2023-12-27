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
        fun List<Set<Point3D>>.step(): Pair<List<Set<Point3D>>, Int> {
            val new = mutableListOf<Set<Point3D>>()
            val fallen = hashSetOf<Point3D>()
            var a = 0

            for (b in this.sortedBy { b -> b.minOf { it.z } }) {
                var cb = b
                while (true) {
                    val down = cb.mapTo(hashSetOf()) { p -> p.copy(z = p.z - 1) }
                    if (down.any { it in fallen || it.z <= 0 }) {
                        new += cb
                        fallen += cb
                        if (cb != b) a++
                        break
                    }

                    cb = down
                }
            }

            return new to a
        }

        val bricks = input.map {
            val coordinateValues = it.replace("~", ",").split(",").map { it.toInt() }
            Brick(
                coordinateValues[0]..coordinateValues[3],
                coordinateValues[1]..coordinateValues[4],
                coordinateValues[2]..coordinateValues[5]
            )
        }.sortedBy { it.z.first }.toMutableList()

        val brickPoints = bricks.map { brick ->
            val pointSet = hashSetOf<Point3D>()

            for (x in brick.x.first..brick.x.last) {
                for (y in brick.y.first..brick.y.last) {
                    for (z in brick.z.first..brick.z.last) {
                        pointSet += Point3D(x, y, z)
                    }
                }
            }

            pointSet
        }

        val (s) = brickPoints.step()
        val t = s.map { b ->
            val n = s.minusElement(b)
            n.step().second
        }
        return t.sum()
    }

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
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

data class Point3D(val x: Int, val y: Int, val z: Int)