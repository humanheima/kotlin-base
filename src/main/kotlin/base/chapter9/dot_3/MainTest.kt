package base.chapter9.dot_3

/**
 * Crete by dumingwei on 2019-09-28
 * Desc: 测试协变
 *
 */


interface Producer<out T> {

    fun produce(): T
}

fun printContents(list: List<Any>) {
    println(list.joinToString())
}

fun addAnswer(list: MutableList<Any>) {
    list.add(42)
}

fun enumerateCats(f: (Cat) -> Number) {

}

fun Animal.getIndex(): Int {
    return 0
}


fun main() {

    enumerateCats(Animal::getIndex)
    printContents(listOf("abc", "bac"))

    val list: MutableList<String> = mutableListOf("abc", "bacd")
    //addAnswer(list)
    //注释2处，输出长度最长的字符串
    //println(list.maxBy { it.length })


}