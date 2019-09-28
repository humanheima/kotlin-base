package base.chapter9.dot_3

/**
 * Crete by dumingwei on 2019-09-28
 * Desc: 测试协变
 *
 */

fun printContents(list: List<Any>) {
    println(list.joinToString())
}

fun addAnswer(list: MutableList<Any>) {
    list.add(42)
}


open class Animal {

    fun fead() {
        println("喂养小动物")
    }
}

class Herd<out T : Animal> {

    val list = listOf<T>()

    val size: Int get() = list.size

    operator fun get(i: Int): T {
        return list[i]
    }
}

fun feedAll(animals: Herd<Animal>) {

    for (i in 0 until animals.size) {
        animals[i].fead()
    }
}

class Cat : Animal() {

    fun cleanLitter() {

    }
}

fun takeCatofCates(cats: Herd<Cat>) {

    for (i in 0 until cats.size) {
        feedAll(cats)
    }
}

fun main() {
    printContents(listOf("abc", "bac"))

    val list: MutableList<String> = mutableListOf("abc", "bac")
    println(list.maxBy { it.length })

}