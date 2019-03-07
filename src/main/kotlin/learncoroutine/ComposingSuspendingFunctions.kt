package learncoroutine

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * 组合挂起函数
 */

/**
 * 顺序执行挂起函数
 */
fun main0(args: Array<String>) = runBlocking {

    val time = measureTimeMillis {
        //默认挂起函数会顺序执行
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("The answer is ${one + two}")
    }
    println("Completed in $time ms")
}

/**
 * 并行执行挂起函数
 */
fun main1(args: Array<String>) = runBlocking {

    val time = measureTimeMillis {
        //默认挂起函数会顺序执行
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

/**
 * 惰性启动的async
 * 当只有调用start或者调用返回的Deferred对象的await方法才会启动协程
 */
fun main2(args: Array<String>) = runBlocking {

    val time = measureTimeMillis {
        //默认挂起函数会顺序执行
        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        //试着不调用start方法，看看耗时
        one.start()
        two.start()
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}


/**
 * async 风格的函数
 * 在kotlin的协程中使用强烈不建议这种风格的函数，考虑一下下面这段代码
 * val one = somethingUsefulOneAsync()
 * //... 其他代码逻辑，如果出错了
 * one.await()
 * 如果val one = somethingUsefulOneAsync()和one.await()之间的代码逻辑出错了，并抛出了一个异常，
 * 那么one.await()就不会被调用。那么这个时候somethingUsefulOneAsync()会一直在后台运行
 *
 */

fun mainm3() {
    val time = measureTimeMillis {
        // we can initiate async actions outside of a coroutine
        val one = somethingUsefulOneAsync()
        val two = somethingUsefulTwoAsync()
        // but waiting for a result must involve either suspending or blocking.
        // here we use `runBlocking { ... }` to block the main thread while waiting for the result
        runBlocking {
            println("The answer is ${one.await() + two.await()}")
        }
    }
    println("Completed in $time ms")
}

/**
 * 使用 async 的结构化并发
 */
fun mainm4() = runBlocking {
    val time = measureTimeMillis {
        println("The answer is ${concurrentSum()}")
    }
    println("Completed in $time ms")
}

/**
 * async 的结构化并发
 * 如果在concurrentSum方法中出现了异常，在这个coroutineScope中启动的所有协程都会被取消。
 */
suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}

/**
 * 取消始终通过协程的层次结构来进行传递
 * 所有的子协程取消以后，父协程才会取消
 */
fun main() = runBlocking<Unit> {
    try {
        failedConcurrentSum()
    } catch (e: ArithmeticException) {
        println("Computation failed with ArithmeticException")
    }
}

suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE) // 模拟一个长时间的运算
            42
        } finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}

suspend fun doSomethingUsefulOne(): Int {

    delay(1000L)
    return 13

}

suspend fun doSomethingUsefulTwo(): Int {

    delay(1000L)
    return 29

}

// The result type of somethingUsefulOneAsync is Deferred<Int>
fun somethingUsefulOneAsync() = GlobalScope.async {
    doSomethingUsefulOne()
}

// The result type of somethingUsefulTwoAsync is Deferred<Int>
fun somethingUsefulTwoAsync() = GlobalScope.async {
    doSomethingUsefulTwo()
}