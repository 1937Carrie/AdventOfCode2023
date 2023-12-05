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
        val seeds: MutableList<Seed> = mutableListOf()
        val newSeeds = mutableMapOf<String, String>()
        input.first().substringAfter("seeds: ").split(" ").chunked(2).forEach {
            newSeeds[it[0]] = it[1]
        }

        var listOf = ""

        newSeeds.forEach { key, value ->
            for (i in 0..<value.toLong()) {
                listOf += (key.toLong() + i).toString() + " "
            }
        }

        val seedsMatcher = Regex("\\d+").toPattern().matcher(listOf)
        while (seedsMatcher.find()) {
            seeds.add(Seed(seedsMatcher.group().toLong(), input))
        }
        seeds.size.println()

        return seeds.minOf { it.location }
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


