package handbook.eleven

/**
 * Created by dmw on 2019/1/7.
 * Desc:
 */

infix fun Int.add(i: Int): Int = this + i

/**
 * 这个有点厉害啊
 */
infix fun Int.加(i: Int): Int = this + i

fun main(args: Array<String>) {

    println(5 add 10)
    println(5 加 10)
}