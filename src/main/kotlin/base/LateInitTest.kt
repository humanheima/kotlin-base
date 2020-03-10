package base


fun main() {

    val myTest = MyTest()
    myTest.test()
}

class MyTest {

    lateinit var name: String

    fun test() {
        if (this::name.isInitialized) {
            println("已经初始化了")
        } else {
            println("没有初始化")

        }
    }


}