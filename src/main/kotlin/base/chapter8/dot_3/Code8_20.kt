package base.chapter8.dot_3

/**
 * 用一个标签实现局部返回
 */
fun lookForEachAliceOne(people: List<Person>) {
    people.forEach label@{
        if (it.name == "Alice") {
            println("Found!")
            return@label
        }
    }
    //这一行总是会被打印出来
    println("Alice might be somewhere !")
}

fun lookForEachAliceTwo(people: List<Person>) {
    people.forEach {
        if (it.name == "Alice") {
            println("Found!")
            return@forEach
        }
    }
    //这一行总是会被打印出来
    println("Alice might be somewhere !")
}

/**
 * 使用匿名函数
 */
fun lookForEachAliceThree(people: List<Person>) {
    people.forEach(fun(person) {
        if (person.name == "Alice") return
        println("${person.name} is not Alice")
    })
}

fun main() {
    //lookForEachAliceOne(people)
    //lookForEachAliceTwo(people)
    lookForEachAliceThree(people)
}