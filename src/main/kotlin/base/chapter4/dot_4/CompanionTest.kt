package base.chapter4.dot_4

/**
 * Created by dumingwei on 2017/12/31 0031.
 * 伴生对象
 */
data class Person1(val name: String) {

    companion object {
        @JvmStatic
        fun fromJson(jsonText: String): Person1 = Person1("hello")
    }

}


interface Factory<T> {
    fun fromJson(jsonText: String): T
}

data class Person2(val name: String) {

    /**
     * 在伴生对象中实现接口
     */
    companion object : Factory<Person2> {

        override fun fromJson(jsonText: String) = Person2(jsonText)
    }

}

fun <T> loadFromJSON(factory: Factory<T>, jsonText: String): T {

    return factory.fromJson(jsonText)
}

fun main() {
    println(loadFromJSON(Person2, "hello"))
    //println(Person2.fromJson("Alice"))
    //println(Person2.fromJson("hhah"))
}

