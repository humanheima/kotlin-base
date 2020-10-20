package base.chapter8.dot_3

import java.lang.StringBuilder

/**
 * 用一个标签实现局部返回
 */
/*fun lookForEachAliceOne(people: List<Person>) {
    people.forEach label@{
        if (it.name == "Alice") {
            println("Found!")
            return@label
        }
    }
    //这一行总是会被打印出来
    println("Alice might be somewhere !")
}*/

/*fun lookForEachAliceTwo(people: List<Person>) {
    people.forEach {
        if (it.name == "Alice") {
            println("Found!")
            return@forEach
        }
    }
    //这一行总是会被打印出来
    println("Alice might be somewhere !")
}*/

/**
 * 使用匿名函数
 */
fun lookForEachAliceThree(people: List<Person>) {
    people.forEach(fun(person) {
        if (person.name == "Alice") return
        println("${person.name} is not Alice")
    })
}

fun lookForEachAlicefour(people: List<Person>) {
    people.filter(fun(person): Boolean {
        return person.age < 30
    })
}

/*fun labelTest1() {
    loop@ for (i in 1..100) {
        for (j in 1..100) {
            println("for j == $j")
            if (j == 10) break@loop
        }
    }
    println("finish")
}*/

/*
fun foo() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return // 非局部直接返回到 foo() 的调用者
        print(it)
    }
    println("this point is unreachable")
}
*/

/*fun foo1() {
    listOf(1, 2, 3, 4, 5).forEach lit@{
        if (it == 3) return@lit // 局部返回到该 lambda 表达式的调用者，即 forEach 循环
        print(it)
    }
    print(" done with explicit label")
}

fun foo2() {
    listOf(1, 2, 3, 4, 5).forEach {
        if (it == 3) return@forEach // 局部返回到该 lambda 表达式的调用者，即 forEach 循环
        print(it)
    }
    print(" done with explicit label")
}

fun foo3() {
    listOf(1, 2, 3, 4, 5).forEach(fun(value: Int) {
        if (value == 3) return  // 局部返回到匿名函数的调用者，即 forEach 循环
        print(value)
    })
    print(" done with anonymous function")
}

fun foo4() {
    run loop@{
        listOf(1, 2, 3, 4, 5).forEach {
            if (it == 3) return@loop // 从传入 run 的 lambda 表达式非局部返回
            print(it)
        }
    }
    print(" done with nested loop")
}*/

fun foo5() {
    val result = run loop@{
        listOf(1, 2, 3, 4, 5).forEach {
            if (it == 3) return@loop 1 // 从传入 run 的 lambda 表达式非局部返回
            println(it)
        }
    }
    println(" done with nested loop $result")
}

/*
val b = { age: Int ->
    String
    "result = $age"
}
*/

fun bFunction(param: Int): String {
    return "bFunction $param"
}

val dFunction = ::bFunction

fun myHighMethod(method: (Int) -> String) {

    println(method(1))
}

val bxxx: (Int) -> String = {
    it.toString()
}

fun main() {

    val d = (::bFunction)

    val e = d

    println(bFunction(1))
    println(d(2))
    println(e(3))

    myHighMethod(d)

    myHighMethod(dFunction)

    //lookForEachAliceOne(people)
    //lookForEachAliceTwo(people)
    lookForEachAliceThree(people)


    //val people:List<Person> = lookForEachAlicefour(people)

    /*println(StringBuilder().apply sb@{

        listOf(1, 2, 3).apply {
            this@sb.append(this.toString())
        }
    })
*/
    /*val result = people.filter(fun(person): Boolean {
        return person.age < 30
    })
    println(result)*/

    //labelTest1()
    //foo()
    //foo1()
    //foo2()
    //foo3()

    //foo4()
    //foo5()

    //println(System.currentTimeMillis() + 86400000 * 8 + 60000)

}