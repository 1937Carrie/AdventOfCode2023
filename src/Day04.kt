fun main(){
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val replaced = it.replace(Regex("Card\\s+\\d+:"), "").split("|")
            val winningNumbers = replaced[0].replace(Regex("^\\s"), "")
            val yourNumbers = replaced[1].replace(Regex("^\\s"), "")
            val matcher = Regex("\\d+").toPattern().matcher(winningNumbers)

            var score = 0
            while (matcher.find()) {
                if (yourNumbers.contains(Regex("(^|\\D)${matcher.group()}(\\D|$)"))) {
                    score = if (score == 0) 1 else score * 2
                }
            }
            score
        }
    }

    fun part2(input: List<String>): Int {
        val scratchCards = mutableListOf<Card>()
        repeat(input.size) {
            scratchCards.add(Card(it + 1, 1, mutableListOf()))
        }
        input.forEachIndexed { index, s ->
            val replaced = s.replace(Regex("Card\\s+\\d+:"), "").split("|")
            val winningNumbers = replaced[0].replace(Regex("^\\s"), "")
            val yourNumbers = replaced[1].replace(Regex("^\\s"), "")
            val matcher = Regex("\\d+").toPattern().matcher(winningNumbers)

            var score = index + 2
            while (matcher.find()) {
                if (yourNumbers.contains(Regex("(^|\\D)${matcher.group()}(\\D|$)"))) {
                    val matchingNumbers = scratchCards[index].matchingNumbers
                    matchingNumbers.add(score)
                    scratchCards[index] = scratchCards[index].copy(matchingNumbers = matchingNumbers)
                    score++
                }
            }
            for (i in 0..<index) {
                if (scratchCards[i].matchingNumbers.contains(index + 1)) {
                    scratchCards[index] =
                        scratchCards[index].copy(quantity = scratchCards[index].quantity + scratchCards[i].quantity)
                }
            }
        }
        return scratchCards.sumOf { it.quantity }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

data class Card(val number: Int, val quantity: Int, val matchingNumbers: MutableList<Int>)