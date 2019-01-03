package handbook.four

/**
 * Created by dmw on 2019/1/3.
 * Desc:
 */
fun main(args: Array<String>) {

    var identityFunction = sum("identity")
    println(identityFunction(1, 10))     //  55

    var squareFunction = sum("square")
    println(squareFunction(1, 10))       //  385

    var cubeFunction = sum("cube")
    println(cubeFunction(1, 10))         //  3025

}

fun sum(type: String): (Int, Int) -> Int {
    val identity = { x: Int -> x }
    val square = { x: Int -> x * x }
    val cube = { x: Int -> x * x * x }

    return when (type) {
        "identity" -> return { a, b -> sum(a, b, identity) }
        "square" -> return { a, b -> sum(a, b, square) }
        "cube" -> { a, b -> sum(a, b, cube) }
        else -> { a, b -> sum(a, b, identity) }
    }

}

fun sum(a: Int, b: Int, term: (Int) -> Int): Int {
    var sum = 0
    for (i in a..b) {
        sum += term(i)
    }
    return sum
}
