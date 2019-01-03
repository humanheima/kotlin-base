package handbook.five

/**
 *
 * Created by dumingwei on 2019/1/3
 *
 * Desc:
 */
fun main(args: Array<String>) {
    val newList = listOf(5, 12, 8, 33)
        .flatMap { it -> listOf(it, it + 1) }
    println(newList)

    //使用sequence
    sequenceOf(5, 12, 8, 33)
        .filter { it > 10 }
        .forEach { print(it) }
}