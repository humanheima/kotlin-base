package base


fun main(args: Array<String>) {

    val testProtected: TestProtected = TestProtected()
    //val method = testProtected::class.java.getDeclaredMethod("onMessagEvent")
    val method = testProtected.javaClass.getDeclaredMethod("onMessagEvent")

    println(method.toString())
}

class TestProtected {

    protected fun onMessagEvent() {

        print("hello world")
    }
}