package handbook.twenty_three

/**
 * Created by dmw on 2019/1/8.
 * Desc:
 */

fun main(args: Array<String>) {

    val sum1 = { x: Int, y: Int ->
        x + y
    }

    val sum2: Int.(Int) -> Int = {
        this + it
    }

    println(sum1(3, 5))
    println(3.sum2(5))
}