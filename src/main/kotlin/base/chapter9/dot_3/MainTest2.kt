package base.chapter9.dot_3

/**
 * Crete by dumingwei on 2019-09-28
 * Desc:
 *
 */

fun <T> copyData(source: MutableList<T>, destination: MutableList<T>) {
    for (item in source) {
        destination.add(item)
    }
}

/**
 * 来源的元素类型是目标元素类型的子类型
 */
fun <T : R, R> copyData1(source: MutableList<T>, destination: MutableList<R>) {
    for (item in source) {
        destination.add(item)
    }
}

/**
 *  MutableList<out T> 和Java中的MutableList<? extends T> 是一个意思
 */
fun <T> copyData2(source: MutableList<out T>, destination: MutableList<T>) {
    for (item in source) {
        destination.add(item)
    }
}

/**
 *  MutableList<in T> 对应到 Java的 MutableList<? super T>
 */
fun <T> copyData3(source: MutableList<out T>, destination: MutableList<in T>) {
    for (item in source) {
        destination.add(item)
    }
}

val list: MutableList<Any?> = mutableListOf('a', 1, "qwe")

val unkonownList: MutableList<*> = mutableListOf('a', 1, "qwe")


fun main(args: Array<String>) {

    list.add(3)
    list.get(0)

    println(unkonownList)
    val ints = mutableListOf(1, 2, 3)
    val anyItems = mutableListOf<Any>()

    copyData1(ints, anyItems)

    copyData2(ints, anyItems)

    copyData3(ints, anyItems)
    println(anyItems)

    val list: MutableList< Number> = mutableListOf()
    list.add(3)
    list.add(3.2)


}