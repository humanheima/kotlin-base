package base.chapter10



/**
 * Created by dumingwei on 2018/1/17 0017.
 */
class Person(val name: String, val age: Int) {

}

fun main(args: Array<String>) {
    val person = Person("Alice", 29)
    val kClass = person.javaClass.kotlin
    println(kClass.simpleName)

}