package handbook.nine

/**
 * Created by dmw on 2019/1/7.
 * Desc:
 */

fun main(args: Array<String>) {
    println(Extension3().text)
}

class Extension3

val Extension3.text: String
    get() = "Hello world"