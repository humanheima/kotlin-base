package learncoroutine


import kotlinx.coroutines.*


/**
 * 协程的取消是 协作 的。一段协程代码必须协作才能被取消。所有 kotlinx.coroutines 中的挂起函数都是 可被取消的 。
 * 它们检查协程的取消， 并在取消时抛出 CancellationException。 然而，如果协程正在执行计算任务，并且没有检查取消的话，
 * 那么它是不能被取消的，就如如下示例代码所示：
 * 输出0，1，2以后并不会退出，输出3，4以后才可以结束。
 */
fun main0() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消一个任务并且等待它结束
    println("main: Now I can quit.")
}

/**
 * 使计算代码可取消
 */
fun main1() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        while (isActive) { // 一个执行计算的循环，只是为了占用 CPU
            // 每秒打印消息两次
            if (System.currentTimeMillis() >= nextPrintTime) {
                println("I'm sleeping ${i++} ...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L) // 等待一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消一个任务并且等待它结束
    println("main: Now I can quit.")
}

/**
 * 在 finally 中释放资源
 */
fun main2() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            println("I'm running finally")
        }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该任务并且等待它结束，会等待finally块中的代码执行完毕。
    println("main: Now I can quit.")
}

/**
 * 运行不能取消的代码块
 */
fun main3() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        } finally {
            withContext(NonCancellable) {
                println("I'm running finally")
                delay(1000L)
                println("And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // 取消该任务并等待它结束
    println("main: Now I can quit.")
}

/**
 * 超时 会抛出异常 TimeoutCancellationException
 */
fun main4() = runBlocking {
    withTimeout(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}
fun main() = runBlocking {
    val result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("I'm sleeping $i ...")
            delay(500L)
        }

    }
    println("Result is $result")
}


/*
fun main() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("I'm sleeping $i...")
            delay(500L)
        }
    }

    delay(1300L) // 延迟一段时间
    println("main: I'm tired of waiting!")
    job.cancel() // 取消该任务
    job.join() // 等待任务执行结束
    println("main: Now I can quit.")
}
*/



