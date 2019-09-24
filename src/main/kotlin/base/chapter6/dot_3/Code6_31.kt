package base.chapter6.dot_3

fun main(args: Array<String>) {

    val strings = listOf("a", "b", "c")
    println("%s,%s,%s".format(*strings.toTypedArray()))

    val array = intArrayOf(1, 2, 3, 4)
    array.forEachIndexed { index, i -> println("index = $index , i = $i") }

}