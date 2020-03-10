package base.chapter8.dot_2

import base.chapter8.dot_3.Person

/**
 * Crete by dumingwei on 2020-03-10
 * Desc:
 *
 */
fun main() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    val message = people.asSequence().filter { it.age < 30 }
    println(message)
}
