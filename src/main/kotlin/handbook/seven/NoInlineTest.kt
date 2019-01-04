package handbook.seven

/**
 *
 * Created by dumingwei on 2019/1/4
 *
 * Desc:使用noinline来修饰不需要内联的函数类型的参数
 */
fun doSomething1() {
    println("do something with inline")
}

fun doSomething2() {
    println("do something with noinline")
}

/**
 * 下面的例子 noinlineExample 函数有两个参数，由于 noinlineExample 函数使用了 inline，所以第一个参数默认使用了 inline，
 * 而第二个参数使用了 noinline。
 */
inline fun noinlineExample(something1: () -> Unit, noinline something2: () -> Unit) {
    something1.invoke()
    something2.invoke()
}

fun main(args: Array<String>) {
    noinlineExample(
        ::doSomething1,
        ::doSomething2
    )
}