package base

/**
 * Crete by dumingwei on 2020-03-10
 * Desc:
 *
 */
fun main(args: Array<String>) {

    println(String.format("hello %1\$s", null))

    val bean = TestProtected("hello")
}

class TestProtected(name: String) {


}

class Chile : Parent() {

    override fun method() {
        super.method()
    }
}

open class Parent {

   open fun method() {

    }

}
