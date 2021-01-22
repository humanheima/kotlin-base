package base.chapter9.dot_3


/**
 * Crete by dumingwei on 2019-09-28
 * Desc: 逆变
 *
 */


interface MyComparator<in T> {

    public fun compare(a: T, b: T): Int
}

fun main(args: Array<String>) {

    val anyComparator: MyComparator<Any> = object : MyComparator<Any> {
        override fun compare(a: Any, b: Any): Int {
            return a.hashCode() - b.hashCode()
        }
    }

    var stringComparator: MyComparator<String> = object : MyComparator<String> {
        override fun compare(a: String, b: String): Int {
            return a.length - b.length
        }
    }

    stringComparator = anyComparator


}

