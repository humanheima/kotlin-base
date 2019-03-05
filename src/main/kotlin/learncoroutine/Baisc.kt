package learncoroutine

import kotlinx.coroutines.*


/*fun main() = runBlocking<Unit> {
    repeat(100_000) {
        // 启动大量的协程
        launch {
            delay(1000L)
            print(".")
        }
    }
}*/

private fun fun1() {
    GlobalScope.launch {
        // 在后台启动一个新的协程并继续
        delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
        println("World!") // 在延迟后打印输出
    }
    println("Hello,") // 协程已在等待时主线程还在继续
    /**
     *阻塞主线程 2 秒钟来保证 JVM 存活,这里为什么要等待2秒呢，因为如果不等待2秒，主线程就结束了，jvm也就停止了，
     * 那么协程中的代码就无法运行了。
     */
    Thread.sleep(2000L)
}

private fun fun2() {
    GlobalScope.launch {
        // 在后台启动一个新的协程并继续
        delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
        println("World!") // 在延迟后打印输出
    }
    println("Hello,") // 协程已在等待时主线程还在继续
    runBlocking {
        // 调用了 runBlocking 的主线程会一直 阻塞 直到 runBlocking 内部的协程执行完毕。
        delay(2000L)
    }
}

/**
 * 等待一个作业
 */
private fun fun3() {
    runBlocking {
        val job = GlobalScope.launch {
            delay(1000)
            println("world!fun3!")
        }
        println("Hello,")
        job.join()
        println("fun3 end")
    }
}


/**
 * 这是一个外部协程
 * 外部协程（示例中的 runBlocking）直到在其作用域中启动的所有协程都执行完毕后才会结束。
 */
/*fun main() = runBlocking {
    */
/**
 * 这是一个内部协程
 *//*
    launch {
        doWorld()
    }
    println("Hello,")
    delay(2000)
}*/

/**
 * 挂起函数
 */
private suspend fun doWorld() {
    delay(1000)
    println("world.")
}

/*
fun main(args: Array<String>) {

    //test0()
    //test1()
    fun3()
}
*/


/**
 * 作用域构建器
 */
fun main() = runBlocking {
    // this: CoroutineScope
    launch {
        delay(200L)
        println("Task from runBlocking")
    }

    coroutineScope {
        // 创建一个新的协程作用域
        launch {
            delay(500L)
            println("Task from nested launch")
        }

        delay(100L)
        println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
    }

    println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
}


private fun test0() {
    GlobalScope.launch {
        delay(1000)
        println("world!")
    }
    println("Hello,")
    Thread.sleep(2000)
}

