package base


//修饰顶层变量
val topInt: Int by lazy {
    1
}

fun main() {


    val byLazyTest = ByLazyTest()
    byLazyTest.printName()
}

class ByLazyTest {

    //修饰成员变量
    private val name: String by lazy {
        "dmw"
    }

    fun printName() {
        //修饰局部变量
        val address: String by lazy {
            "dmw"
        }

        print(this.name)
    }

}
