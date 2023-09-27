package base

/**
 * Created by dumingwei on 2021/11/2.
 *
 * Desc:
 */

fun main() {
    println("Hello world")

    val list = listOf(1, 2, 3, 4, 5)
    val list2 = listOf(1, 2, 3, 4, 5)
    val iterator = list.iterator()
    while (iterator.hasNext()) {
        val next =iterator.next()
        print("next=$next")
        val iterator2 = list2.iterator()
        while (iterator2.hasNext()) {
            println("iterator.next(),iterator2.next()=${iterator2.next()}")
            break
        }
    }


}


class Category1 {

    var hot: Boolean = false
    var count: Int = -1
    var name: String? = null

}


