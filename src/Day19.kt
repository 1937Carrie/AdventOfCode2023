fun main() {
    fun part1(workflows: Map<String, Workflow>, ratings: List<Rating>): Int {
        return ratings.sumOf { it.score(workflows.getValue("in"), workflows) }
    }

    fun part2(workflows: Map<String, Workflow>): Long {
        val ratings = mapOf('x' to (1..4000), 'm' to (1..4000), 'a' to (1..4000), 's' to (1..4000))
        return combinations("in", workflows, ratings)
    }

    val input = readInput("Day19").joinToString("\n")
    val workflows = input.substringBefore("\n\n").lines().map { Workflow.getWorkflow(it) }.associateBy { it.name }
    val ratings = input.substringAfter("\n\n").lines().map { Rating.getRating(it) }
    part1(workflows, ratings).println()
    part2(workflows).println()
}

private fun combinations(result: String, workflows: Map<String, Workflow>, ratings: Map<Char, IntRange>): Long {
    return when (result) {
        "R" -> 0
        "A" -> ratings.values.map { it.size().toLong() }.reduce(Long::times)
        else -> {
            val newRanges = ratings.toMutableMap()

            workflows.getValue(result).rules.sumOf { rule ->
                when (rule) {
                    is Rule.Unconditional -> combinations(rule.result, workflows, newRanges)
                    is Rule.Conditional -> {
                        val newRange = newRanges.getValue(rule.category).merge(rule.range())
                        val newReversed = newRanges.getValue(rule.category).merge(rule.reversedRange())

                        newRanges[rule.category] = newRange
                        combinations(rule.result, workflows, newRanges).also { newRanges[rule.category] = newReversed }
                    }
                }
            }
        }
    }
}

private fun IntRange.merge(other: IntRange) = (maxOf(first, other.first)..minOf(last, other.last))

private fun IntRange.size() = last - start + 1

private fun Rating.score(workflow: Workflow, workflows: Map<String, Workflow>): Int {
    val rule = workflow.rules.first { it.matches(this) }
    return when (rule.result) {
        "R" -> 0
        "A" -> categories.values.sum()
        else -> score(workflows.getValue(rule.result), workflows)
    }
}

private data class Workflow(val name: String, val rules: List<Rule>) {
    companion object {
        fun getWorkflow(str: String): Workflow {
            val name = str.substringBefore("{")
            val rules = str.substringAfter("{").substringBefore("}").split(",").map { Rule.from(it) }
            return Workflow(name, rules)
        }
    }
}

private sealed class Rule {
    abstract val result: String

    data class Conditional(val category: Char, val comparisonSign: Char, val rightPart: Int, override val result: String) : Rule() {
        fun range(): IntRange = if (comparisonSign == '<') (1..<rightPart) else (rightPart + 1..4000)
        fun reversedRange(): IntRange = if (comparisonSign == '<') (rightPart..4000) else (1..rightPart)
    }

    data class Unconditional(override val result: String) : Rule()

    fun matches(rating: Rating): Boolean {
        return when (this) {
            is Unconditional -> true
            is Conditional -> {
                when (comparisonSign) {
                    '>' -> rating.categories.getValue(category) > rightPart
                    '<' -> rating.categories.getValue(category) < rightPart
                    else -> error("Unsupported operation: $comparisonSign")
                }
            }
        }
    }

    companion object {
        fun from(str: String): Rule {
            return if (':' in str) {
                val condition = str.substringBefore(":")
                val result = str.substringAfter(":")
                Conditional(condition[0], condition[1], condition.substring(2).toInt(), result)
            } else {
                Unconditional(str)
            }
        }
    }
}

private data class Rating(val categories: Map<Char, Int>) {
    companion object {
        fun getRating(str: String): Rating {
            val categories = str.drop(1).dropLast(1).split(",").associate {
                it.substringBefore("=").single() to it.substringAfter("=").toInt()
            }
            return Rating(categories)
        }
    }
}
