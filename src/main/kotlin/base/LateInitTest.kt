package base


lateinit var topVariable: String
fun main() {


    val myTest = MyTest()
    //myTest.check()
    //为什么不能在这里检车MyTest的name属性是否被初始化了呢？还不清楚
    //println(myTest::name.isInitialized)

}


class MyTest {


    lateinit var name: String


    /*fun check() {
        if (this::name.isInitialized) {
            println(name)
        } else {
            println("name 还没有初始化")

        }
    }*/
}