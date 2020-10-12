package base.chapter2.dot_4

/**
 * Created by dumingwei on 2017/12/21 0021.
 */
fun main(args: Array<String>) {
    println(recognize('8'))

    /**
     * 查看反编译出来的代码等价于 "Java" <= "Kotlin" && "Kotlin" <= "Scala"
     * 因为字符串是实现了Comparable的
     */
    println("Kotlin" in "Java".."Scala")
    println("kotlin" in setOf("JAVA", "Android"))
}

fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "It's a digit!"
    in 'a'..'z', in 'A'..'Z' -> "It's a letter"
    else -> "I don't know"
}