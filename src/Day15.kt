fun main() {
    fun part1(input: List<String>) = input.first().split(',').sumOf { it.hash() }

    fun part2(input: List<String>): Int {
        val boxes = mutableMapOf<Int, MutableList<Label>>()
        input.first().split(',').forEach { currentString ->
            if (currentString.contains(Regex("\\d"))) {
                val label = currentString.substringBefore("=")
                val focalLength = currentString.substringAfter("=").toInt()

                if (boxes.containsKey(label.hash()).not()) {
                    boxes[label.hash()] = mutableListOf(
                        Label(label, currentString.substringAfter("=").toInt())
                    )
                } else {
                    val box = boxes[label.hash()]!!
                    if (box.any { it.label == label }) {
                        val indexOfLabel = box.indexOf(box.first { it.label == label })
                        box[indexOfLabel] = box[indexOfLabel].copy(focalLength = focalLength)
                    } else {
                        box.add(Label(label, focalLength))
                    }
                }
            } else {
                val substringBefore = currentString.substringBefore("-")
                if (boxes.any { it.key == substringBefore.hash() } &&
                    (boxes[substringBefore.hash()]?.filter { it.label == substringBefore }?.size ?: 0) > 0
                ) {
                    val box = boxes[substringBefore.hash()]!!
                    box.remove(box.first { it.label == substringBefore })
                    boxes[substringBefore.hash()] = box

                }
            }
        }
        var sum = 0
        boxes.forEach { (key, labels) ->
            var index = 1
            labels.forEach {
                sum += (key + 1) * index++ * it.focalLength
            }
        }
        return sum
    }

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}

data class Label(val label: String, val focalLength: Int)

fun String.hash(): Int = this.fold(0) { acc, ch -> (acc + ch.code) * 17 % 256 }