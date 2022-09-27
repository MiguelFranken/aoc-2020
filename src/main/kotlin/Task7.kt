import java.io.File

fun main() {
    val lines = File("src/main/resources/Task7.txt").readLines()

    val bags = lines.associate {
        val (bagKey, bagContentDescription) = it.split("contain")
        val bagColor = bagKey.substringBefore(" bags")

        val bagContent = bagContentDescription
            .removeSuffix(".")
            .split(", ")
            .map { quantity -> quantity.removeSuffix(" bags").removeSuffix(" bag").trim() }
            .mapNotNull { quantity: String ->
                when (quantity) {
                    "no other" -> null
                    else -> Pair(
                        quantity.substringBefore(" ").toInt(),
                        quantity.substringAfter(" ")
                    )
                }
            }

        Pair(bagColor, bagContent)
    }

    task1(bags)
    task2(bags)
}

fun task1(bagsUnmapped: Map<String, List<Pair<Int, String>>>) {
    val bags = bagsUnmapped.mapValues { (_, bagContent) ->
        bagContent.map { p -> p.second }
    }

    fun findSuperBags(subBags: Iterable<String>) = subBags.fold(setOf<String>()) { acc, curr ->
        val superBags = bags.filter { (_, content) ->
            content.contains(curr)
        }.map { it.key }.toSet()

        acc union superBags
    }

    var curr = setOf("shiny gold")
    val superBags = mutableSetOf<String>()
    while (curr.isNotEmpty()) {
        curr = findSuperBags(curr)
        superBags += curr
    }

    println("${superBags.size} bags can eventually contain at least one 'shiny gold' bag")
    require(superBags.size == 259)
}

fun task2(bags: Map<String, List<Pair<Int, String>>>) {
    fun countBags(color: String): Int =
        bags[color]?.let { subBags ->
            subBags.sumOf { it.first } + subBags.sumOf { (quantity, color) ->
                quantity * countBags(color)
            }
        } ?: 0

    val count = countBags("shiny gold")
    require(count == 45018)
    println("$count individual bags are required inside a single 'shiny gold' bag")
}
