package handbook.eight

import java.util.regex.Pattern

/**
 *
 * Created by dumingwei on 2019/1/7
 *
 * Desc:扩展函数
 * 1 扩展函数本质上并不是对原先的类新增一个方法，它是以静态导入的方式来实现的。
 * 2 扩展函数跟原先的函数重名，并且参数都一样时，扩展函数会失效，调用的依旧是原先的函数。
 * 3 扩展函数不具备多态性。
 * 4 Java 也能调用 Kotlin 的扩展函数，可以把它当成是一个工具类来使用。
 */
fun String.checkEmail(): Boolean {
    val emailPattern = "[a-zA-Z0-9][a-zA-Z0-9._-]{2,16}[a-zA-Z0-9]@[a-zA-Z0-9]+.[a-zA-Z0-9]+"
    return Pattern.matches(emailPattern, this)
}

fun main(args: Array<String>) {
    //println("fengzhizi715@126.com".checkEmail())
    var base = Base()
    var child = Child()

    executeFoo(base)
    executeFoo(child)

    val result = "hello".apply {
        println(this + " world")
        this + "world"
    }
    println(result)
}

open class Base

class Child : Base()

fun Base.foo() = println("this is from base") // 父类的扩展函数 foo

fun Child.foo() = println("this is from child") // 子类的扩展函数 foo

fun executeFoo(base: Base) = base.foo()
