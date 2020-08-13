package learncoroutine

import kotlinx.coroutines.*

/**
 * Crete by dumingwei on 2019-07-20
 * Desc: 协程基础
 *
 */
fun main() = runBlocking {
    //funX()
    //fun3()
    //fun4()
    fun5()
}

private suspend fun funX() {
    val currentMillis = System.currentTimeMillis()
    val retVal1 = downloadTask1()
    val retVal2 = downloadTask2()
    val retVal3 = downloadTask3()
    println("All tasks downloaded! ${retVal1}, ${retVal2}, $retVal3 in ${(System.currentTimeMillis() - currentMillis) / 1000} seconds")
}
/*fun main() = runBlocking<Unit> {
    repeat(100_000) {
        // 启动大量的协程
        launch {
            delay(1000L)
            print(".")
        }
    }
}*/

private suspend fun downloadTask1(): String {
    kotlinx.coroutines.delay(5000);
    return "Complete";
}

// Task 1 will take 8 seconds to complete download
private suspend fun downloadTask2(): Int {
    kotlinx.coroutines.delay(8000);
    return 100;
}

// Task 1 will take 5 seconds to complete download
private suspend fun downloadTask3(): Float {
    kotlinx.coroutines.delay(5000);
    return 4.0f;
}

/**
 * Your first coroutine
 */
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
     * 1.让主线程睡眠500ms，则协程中的`World!`无法输出。
     * 2.让协程阻塞3000ms，则协程中的`World!`无法输出。
     */
    //Thread.sleep(500L)
    Thread.sleep(2000L)
}

/**
 * Bridging blocking and non-blocking worlds
 */
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
 * 结构化并发 Structured concurrency，就是说我们可以在一个协程内部启动多个子协程。
 * 外部协程（示例中的 runBlocking）直到在其作用域中启动的所有子协程都执行完毕后才会结束。
 */
private fun fun4() = runBlocking {
    /**
     * 这是一个内部协程
     */
    launch {
        doWorld()
    }
    println("Hello,")
    delay(2000L)
}

/**
 * 挂起函数
 */
private suspend fun doWorld() {
    delay(1000L)
    println("world.")
}


/**
 * 作用域构建器，这个有点意思，得好好研究
 *
 * runBlocking会阻塞当前线程，coroutineScope不会阻塞当前线程
 */
private fun fun5() = runBlocking { // this: CoroutineScope
    launch {
        delay(200L)
        log("Task from runBlocking")
    }

    coroutineScope {
        // 创建一个新的协程作用域
        launch {
            delay(500L)
            log("Task from nested launch")
        }

        delay(100L)
        log("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
    }

    log("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
}


private fun fun6() = runBlocking {
    GlobalScope.launch {
        repeat(1000) { i ->
            println("I am sleeping $i")
            delay(500L)
        }
    }
    delay(1300L)
}

