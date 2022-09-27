import java.io.File

val nl: String = System.lineSeparator()

fun Sequence<List<Char>>.sumOfSizes() = map(List<Char>::size).sum()

fun main() {
    alternative1()
    alternative2()
}

fun alternative1() {
    val groups = File("src/main/resources/Task6.txt")
        .readText()
        .trim()
        .split("$nl$nl")
        .asSequence()
        .map { it.split(nl).map { c -> c.toList() } }

    val part1 = groups
        .map { it.flatten().toSet().toList() }
        .sumOfSizes()

    println("Part 1: $part1")
    require(part1 == 6437)

    val part2 = groups
        .map { it.flatten().toSet().filter { o -> it.flatten().count { c -> c == o } == it.size } }
        .sumOfSizes()

    println("Part 2: $part2")
    require(part2 == 3229)
}

fun alternative2() {
    fun aggregate(collector: (s1: Set<Char>, s2: Set<Char>) -> Set<Char>): Int =
        File("src/main/resources/Task6.txt")
            .readText()
            .trim()
            .split("$nl$nl")
            .map { group -> group.split(nl).map(String::toSet) }
            .sumOf { it.reduce { a, b -> collector(a, b) }.count() }

    val part1 = aggregate { a, b -> a union b }
    val part2 = aggregate { a, b -> a intersect b }

    require(part1 == 6437)
    require(part2 == 3229)
}
