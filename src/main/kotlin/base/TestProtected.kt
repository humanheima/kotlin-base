package base


fun main(args: Array<String>) {

    println(String.format("hello %1\$s",null))

    val bean = TestProtected("hello")
}

class TestProtected(name: String) {


}

