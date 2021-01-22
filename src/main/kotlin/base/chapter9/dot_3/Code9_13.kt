package base.chapter9.dot_3

/**
 * Created by dumingwei on 2020/10/18.
 *
 * Desc:
 */

open class Animal {

    fun feed() {
        println("喂养小动物")
    }
}


class Cat : Animal() {

    fun cleanLitter() {

    }
}

class Herd1<out T : Animal>(private var animal: T, vararg animals: T) {

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
        animals[i].feed()
    }
}

fun takeCatOfCats(cats: Herd<Cat>) {
    for (i in 0 until cats.size) {
        cats[i].cleanLitter()
    }
    feedAll(cats)
}


fun main() {
    val cats: List<Cat> = listOf()
    val animals: List<Animal> = cats
}


interface Transformer<T> {

    fun transform(t: T): T

}
















