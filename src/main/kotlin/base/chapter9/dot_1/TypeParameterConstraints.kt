package base.chapter9.dot_1

/**
 * Created by dumingwei on 2018/1/7 0007.
 */
fun <T : Comparable<T>> max(first: T, second: T): T {

    //实现了Comparable接口的类，可以直接用运算符进行比较大小
    return if (first > second) first else second
}

/**
 * 为类型参数指定多个约束
 *
 */
fun <T> ensureTrailingPeriod(seq: T) where T : CharSequence, T : Appendable {
    if (!seq.endsWith('.'))
        seq.append('.')
}

class Processor<T> where T : CharSequence, T : Appendable {

    lateinit var t: T
    fun process(value: T) {
        t = value
        t.append('c')
        value.hashCode()
    }
}

fun main(args: Array<String>) {
    val max = max("kotlin", "java")
    println("max = $max")

    val processor: Processor<StringBuilder> = Processor()
    processor.process(StringBuilder())
    "java".substring(3)
    val list = listOf<String>()

    val helloWorld = StringBuilder("Hello World")
    ensureTrailingPeriod(helloWorld)
    println(helloWorld)
}