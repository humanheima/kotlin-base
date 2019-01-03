package handbook.two

import io.reactivex.Observable

/**
 * Created by dmw on 2019/1/3.
 * Desc:
 */

fun main(args: Array<String>) {

    Observable.just(1)
        .compose(RxJavaUtils.preventDuplicateClicksTransformer())
        .subscribe { it -> println(it) }

    val list = toList("java", "kotlin", "scala", "groovy")
    println(list)

    /**
     * toList() 也可以传递数组，不过不能像 Java 那样直接传递数组。
     * 需要使用展开运算符*(在参数名前加*)，它表示解包数组，能够让数组中的每个元素在函数中被作为单独的参数。
     */
    val array = arrayOf("java", "kotlin", "scala", "groovy")
    val listArray = toList(*array)
    println(listArray)

    val list2 = toList2("java", "kotlin", "scala", "groovy", str = "hello")
    println(list2)

    println(sumWithTailrec(100, 0))

}

/**
 * Kotlin 1.3 之后引入的一种更简单的无参 main 函数，简化了 main 函数的写法
 */
/*
fun main() {
    println("Hello Kotlin")
}
*/

/**
 * 可变参数
 */
fun <T> toList(vararg items: T): List<T> {

    val result = ArrayList<T>()
    for (item in items) {
        result.add(item)
    }
    return result
}

/**
 * 如果可变参数不是最后一个参数，那么后面的参数需要通过命名参数来传值
 */
fun <T> toList2(vararg items: T, str: String): List<String> {
    val result = ArrayList<String>()
    for (item in items)
        result.add(item.toString())

    result.add(str)
    return result
}

/**
 * 尾递归函数
 */
tailrec fun sumWithTailrec(n: Int, result: Int): Int = if (n <= 0) result else sumWithTailrec(n - 1, result + n)
