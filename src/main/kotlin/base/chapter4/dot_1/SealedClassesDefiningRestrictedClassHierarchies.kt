package base.chapter4.dot_1

import handbook.four.sum

/**
 * Created by dumingwei on 2017/12/22 0022.
 * 定义密封类
 */
sealed class Expr {
    class Num(val value: Int) : Expr()


}

/**
 * 密封类的子类也可以定义在密封类之外
 */
class Sum(val left: Expr, val right: Expr) : Expr()

fun eval(e: Expr): Int =
    when (e) {
        is Expr.Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
    }


sealed class Operation {

    class Add(val value: Int) : Operation()
    class Substract(val value: Int) : Operation()
    class Multiply(val value: Int) : Operation()
    class Divide(val value: Int) : Operation()

    //If a subclass doesn’t keep state, it can just be an object
    object Increment : Operation()

    object Decrement : Operation()

}

fun execute(x: Int, op: Operation) = when (op) {
    is Operation.Add -> x + op.value
    is Operation.Substract -> x - op.value
    is Operation.Multiply -> x * op.value
    is Operation.Divide -> x / op.value
    is Operation.Increment -> x + 1
    is Operation.Decrement -> x - 1
}

fun main(args: Array<String>) {
    println(eval(Sum(Sum(Expr.Num(1), Expr.Num(2)), Expr.Num(3))))
    println(execute(10, Operation.Add(2)))
    println(execute(10, Operation.Increment))
}
