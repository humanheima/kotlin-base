package handbook.twenty_nine

import kotlinx.coroutines.*


fun main(args: Array<String>) {

    // 创建一个协程，并在内部再创建两个协程
    val job = GlobalScope.launch {

        // 第一个使用不同的上下文
        val job1 = GlobalScope.launch {

            println("job1: I have my own context and execute independently!")
            delay(1000)
            println("job1: I am not affected by cancellation of the job")
        }

        // 第二个继承父级上下文
        val job2 = launch(coroutineContext) {

            println("job2: I am a child of the job coroutine")
            delay(1000)
            println("job2: I will not execute this line if my parent job is cancelled")
        }

        job1.join()
        job2.join()
    }

    Thread.sleep(500)

    job.cancel() // 取消job

    Thread.sleep(2000)

}

fun main9(args: Array<String>) = runBlocking(Dispatchers.Default) {

    val jobs = ArrayList<Job>()

    jobs += launch(Dispatchers.Unconfined) {
        // 无限制
        println("'Unconfined': I'm working in thread ${Thread.currentThread().name}")
        delay(500)
        println("'Unconfined': After delay in thread ${Thread.currentThread().name}")
    }

    jobs += launch(coroutineContext) {
        // 使用父级的上下文，也就是 runBlocking 的上下文
        println("'coroutineContext': I'm working in thread ${Thread.currentThread().name}")
    }

    jobs += launch(Dispatchers.Default) {
        println("'Dispatchers.Default': I'm working in thread ${Thread.currentThread().name}")
    }

    jobs += launch {
        println("'default': I'm working in thread ${Thread.currentThread().name}")
    }

    jobs += launch(newSingleThreadContext("MyThread")) {
        // 创建自己的新线程
        println("'MyThread': I'm working in thread ${Thread.currentThread().name}")
    }

    jobs.forEach { it.join() }

}

fun main8(args: Array<String>) = runBlocking {

    val jobs = ArrayList<Job>()

    //表示在被调用的线程中启动协程，直到程序运行到第一个挂起点
    jobs += launch(Dispatchers.Unconfined) {
        // 无限制
        println("'Unconfined': I'm working in thread ${Thread.currentThread().name}")
    }

    jobs += launch(coroutineContext) {
        // 使用父级的上下文，也就是 runBlocking 的上下文
        println("'coroutineContext': I'm working in thread ${Thread.currentThread().name}")
    }

    jobs += launch(Dispatchers.Default) {
        println("'Dispatchers.Default': I'm working in thread ${Thread.currentThread().name}")
    }

    jobs += launch {
        println("'default': I'm working in thread ${Thread.currentThread().name}")
    }

    jobs += launch(newSingleThreadContext("MyThread")) {
        // 创建自己的新线程
        println("'MyThread': I'm working in thread ${Thread.currentThread().name}")
    }

    jobs.forEach { it.join() }
}

fun main7(args: Array<String>) {

    GlobalScope.launch {
        val result1 = withContext(Dispatchers.Default) {
            delay(2000)
            1
        }
        val result2 = withContext(Dispatchers.IO) {
            delay(2000)
            2
        }
        val result = result1 + result2
        println(result)
    }

    Thread.sleep(5000)
}

fun main6(args: Array<String>) = runBlocking {

    val job1 = launch {

        println(1)
        yield()
        println(3)
        yield()
        println(5)
    }

    val job2 = launch {

        println(2)
        yield()
        println(4)
        yield()
        println(6)
    }

    println(0)

    // 无论是否调用以下两句，上面两个协程都会运行
    job1.join()
    job2.join()

}

fun main5(args: Array<String>) = runBlocking {

    println("1:current thread is ${Thread.currentThread().name}")

    GlobalScope.launch {

        println("3: current thread is ${Thread.currentThread().name}")

        delay(1000L)

        println("4: current thread is ${Thread.currentThread().name}")
    }

    println("2: current thread is ${Thread.currentThread().name}")

    Thread.sleep(2000L)

    println("5: current thread is ${Thread.currentThread().name}")
}

fun main4(args: Array<String>) {

    GlobalScope.launch {


        val result: Deferred<Int> = async {
            delay(2000)
            1
        }

        result.invokeOnCompletion {
            if (it != null) {
                println("exception:${it.message}")

            } else {
                println("result is complete")
            }
        }

        result.cancelAndJoin()
        println(result.await())
    }

    println("main method")
    Thread.sleep(5000)
}

fun main3(args: Array<String>) {


    GlobalScope.launch {


        val result1: Deferred<Int> = async {
            delay(2000)
            1
        }

        val result2: Deferred<Int> = async(start = CoroutineStart.LAZY) {
            delay(1000)
            2
        }

        val result = result1.await() + result2.await()
        println("result = $result")
    }

    println("main method")
    Thread.sleep(5000)
}

fun main2(args: Array<String>) {

    GlobalScope.launch {


        val result1: Deferred<Int> = async {
            delay(2000)
            1
        }

        val result2: Deferred<Int> = async {
            delay(1000)
            2
        }

        val result = result1.await() + result2.await()
        println("result$result")
    }

    println("main method")
    Thread.sleep(5000)
}

fun main1(args: Array<String>) {


    val job = GlobalScope.launch {
        delay(1000)
        println("Hello Coroutines!")
    }

    println("main method")
    Thread.sleep(2000)
}
