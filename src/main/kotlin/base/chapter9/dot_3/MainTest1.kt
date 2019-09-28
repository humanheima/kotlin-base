package base.chapter9.dot_3

/**
 * Crete by dumingwei on 2019-09-28
 * Desc: 逆变
 *
 */

//注意 T 前面的 in
interface Comparator<in T> {

    fun compare(e1: T, e2: T): Int

}

fun main(args: Array<String>) {

}