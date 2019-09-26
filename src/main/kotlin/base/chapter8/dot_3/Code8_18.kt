package base.chapter8.dot_3

data class Person(val name: String, val age: Int)

val people = listOf(Person("Alice", 29), Person("Bob", 31))

fun lookForAlice(people: List<Person>) {
    for (person in people) {
        if (person.name == "Alice") {
            println("Found!")
            return
        }
    }
    println("Not found!")
}

/**
 * 非局部返回：如果你在lambda中使用return关键字，它会从调用lambda的函数中返回，并不只从lambda中返回。
 * 这样的return语句叫做非局部返回，因为它从一个比包含return的代码块更大的代码块中返回了。
 *
 * 需要注意的是：只有在lambda作为参数的函数是内联函数的时候才能从更外层的函数返回。
 *
 */
fun lookForEachAlice(people: List<Person>) {
    people.forEach {
        if (it.name == "Alice") {
            println("Found!")
            return
        }
    }
    println("Not found!")
}


fun main(args: Array<String>) {
    //lookForAlice(people)
    lookForEachAlice(people)
}