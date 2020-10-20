package base.chapter9.dot_3


/**
 * Crete by dumingwei on 2019-09-28
 * Desc: 逆变
 *
 */


fun main(args: Array<String>) {

    val anyComparator: Comparator<Any> = Comparator<Any> { o1, o2 ->
        o1.hashCode() - o2.hashCode()
    }
    val list: List<String> = listOf()

    list.sortedWith(anyComparator)
}