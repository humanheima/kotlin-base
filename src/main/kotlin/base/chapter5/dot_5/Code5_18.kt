package base.chapter5.dot_5

/**
 * Created by dumingwei on 2017/12/26 0026.
 */
fun alphabet2() = with(StringBuilder()) {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
    toString()
}

fun main(args: Array<String>) {
    println(alphabet2())
}