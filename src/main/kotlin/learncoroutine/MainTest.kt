package learncoroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 这是一个外部协程
 * 外部协程（示例中的 runBlocking）直到在其作用域中启动的所有协程都执行完毕后才会结束。
 */
fun main() = runBlocking {
    /**
     * 这是一个内部协程
     */
    launch {
        doWorld()
    }
    println("Hello,")
    delay(2000)
}

/**
 * 挂起函数
 */
private suspend fun doWorld() {
    delay(1000)
    println("world.")
}

/*fun main(args: Array<String>) {

    //test0()
    //test1()
    test2()
}*/

/**
 * 等待一个作业
 */
private fun test2() {
    runBlocking {
        val job = GlobalScope.launch {
            delay(1000)
            println("world!test2!")
        }
        println("Hello,")
        job.join()
        println("test2")
    }
}

private fun test0() {
    GlobalScope.launch {
        delay(1000)
        println("world!")
    }
    println("Hello,")
    Thread.sleep(2000)
}

