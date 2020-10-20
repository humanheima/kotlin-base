package base.chapter9.dot_2

/**
 * Created by dumingwei on 2018/1/7 0007.
 */

inline fun <reified T> isA(value: Any) = value is T

inline fun <reified T> isAReified(value: Any): Boolean {
    return value is T
}

inline fun <reified T> Iterable<*>.filterIsInstance(): List<T> {

    val destination = ArrayList<T>()
    for (element in this) {
        if (element is T) {
            destination.add(element)
        }
    }
    return destination
}

fun main(args: Array<String>) {

    println(isAReified<String>("abc"))
    println(isAReified<String>(123))

    val items = listOf("one", 2, "three")
    println(items.filterIsInstance<String>())
}