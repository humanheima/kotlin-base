package handbook.twenty_six

import kotlinx.coroutines.*

/**
 * Created by dmw on 2019/1/9.
 * Desc:
 */

fun main(args: Array<String>) {
    //test0()
    runBlocking {
        val result: Deferred<String> = async { doSomethingTimeout() }
        println("I will got the result ${result.await()}")
    }
}

suspend fun doSomethingTimeout(): String {
    delay(1000)
    return "Result"
}

private fun test0() {
    repeat(100_000) {
        // 启动十万个协程试试
        GlobalScope.launch {
            suspendPrint()
        }
    }
    Thread.sleep(1200) // 等待协程代码的结束
}

suspend fun suspendPrint() {
    delay(1000)
    println("Hello")
}