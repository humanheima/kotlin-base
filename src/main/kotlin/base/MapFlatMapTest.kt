package base

/**
 * Crete by dumingwei on 2020-03-13
 * Desc: 测试map和flat的区别
 *
 */

fun main() {

    val list = listOf(1, 2, 3, 4)
    val result: List<String> = list.map { "Hello $it" }
    println(result)


    val dmw = Person(
        arrayListOf(
            Hobby("读书"),
            Hobby("跑步"),
            Hobby("足球"),
            Hobby("篮球")
        )
    )
    val caixukun = Person(
        arrayListOf(
            Hobby("唱"),
            Hobby("跳"),
            Hobby("rap"),
            Hobby("篮球")
        )
    )

    val personList = listOf(dmw, caixukun)

    val personHobbyList = personList.flatMap {
        it.hobbies
    }

    personHobbyList.forEach {
        print(it.hobbyType + " ")
    }

}

class Person(var hobbies: List<Hobby>)

class Hobby(val hobbyType: String)
