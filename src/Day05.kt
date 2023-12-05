fun main() {
    fun part1(input: List<String>): Long {
        val seeds: MutableList<Seed> = mutableListOf()

        val seedsMatcher = Regex("\\d+").toPattern().matcher(input[0])
        while (seedsMatcher.find()) {
            seeds.add(Seed(seedsMatcher.group().toLong(), input))
        }

        return seeds.minOf { it.location }
    }

    fun part2(input: List<String>): Long {
        val seeds = input.first().substringAfter(" ").split(" ").map { it.toLong() }.chunked(2)
            .map { it.first()..<it.first() + it.last() }
        val categories = input.drop(2).joinToString("\n").split("\n\n").map { section ->
            section.lines().drop(1).associate {
                it.split(" ").map { it.toLong() }.let { (dest, source, length) ->
                    source..(source + length) to dest..(dest + length)
                }
            }
        }

       return seeds.flatMap { seedsRange ->
            categories.fold(listOf(seedsRange)) { aac, map ->
                aac.flatMap {
                    map.entries.mapNotNull { (source, dest) ->
                        (maxOf(source.first, it.first) to minOf(source.last, it.last)).let { (start, end) ->
                            if (start <= end) (dest.first - source.first).let { (start + it)..(end + it) } else null
                        }
                    }
                }
            }
        }.minOf { it.first }
    }

    val input = readInput("Day05")

    part1(input).println()
    part2(input).println() // very long computations. I mean it is more than you expected
}

data class Seed(val seedNumber: Long, private val input: List<String>) {
    val soil = getNumber("seed-to-soil map:", seedNumber)
    val fertilizer = getNumber("soil-to-fertilizer map:", soil)
    val water = getNumber("fertilizer-to-water map:", fertilizer)
    val light = getNumber("water-to-light map:", water)
    val temperature = getNumber("light-to-temperature map:", light)
    val humidity = getNumber("temperature-to-humidity map:", temperature)
    val location = getNumber("humidity-to-location map:", humidity)

    fun getNumber(s: String, numberD: Long): Long {
        val startIndex = input.indexOf(input.find { it == s }) + 1
        val seedToSoilMap = getCertainMap(input, startIndex)
        seedToSoilMap.forEach {
            val split = it.split(Regex("\\s+"))
            if (numberD in split[1].toLong()..<split[1].toLong() + split[2].toLong()) {
                return numberD - split[1].toLong() + split[0].toLong()
            }
        }
        return numberD
    }
}

fun getCertainMap(input: List<String>, startIndex: Int): MutableList<String> {
    val tmp = mutableListOf<String>()
    var endIndex = input.size
    for (i in startIndex..<input.size) {
        if (input[i].isEmpty()) {
            endIndex = i
            break
        }
    }

    for (r in startIndex..<endIndex) {
        if (Regex("\\d+\\s+\\d+\\s+\\d+").find(input[r]) != null) {
            tmp.add(input[r])
        }
    }

    return tmp
}


