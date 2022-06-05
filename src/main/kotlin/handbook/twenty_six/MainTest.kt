package handbook.twenty_six

import kotlinx.coroutines.*

/**
 * Created by dmw on 2019/1/9.
 * Desc: 挂起函数
 */

fun main(args: Array<String>) {

    //useDelay()
    useYield()
    //useWithContext()
    //useCoroutineScope()
    //useDispatchers()
    //parentChildCoroutineScope()
    //parentChildCoroutineScope2()

}


/**
 * 关于父子协程的例子
 */
private fun parentChildCoroutineScope() {

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

/**
 * 下面的代码中，我们会看到父协程会等待子协程执行完。
 */
private fun parentChildCoroutineScope2() {

    runBlocking {
        val job = launch {

            // 子协程
            val job1 = launch(coroutineContext) {

                println("job1 is running")
                delay(1000)
                println("job1 is done")
            }

            // 子协程
            val job2 = launch(coroutineContext) {

                println("job2 is running")
                delay(1500)
                println("job2 is done")
            }

            // 子协程
            val job3 = launch(coroutineContext) {

                println("job3 is running")
                delay(2000)
                println("job3 is done")
            }

            job1.join()
            job2.join()
            job3.join()
        }

        job.join()

        println("all the jobs is complete")
    }

}

/**
 * 每次执行，输出结果顺序可能会不同
 */
private fun useDispatchers() {
    runBlocking {
        val jobs = ArrayList<Job>()

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
}

private fun useCoroutineScope() {
    GlobalScope.launch {

        val result1 = withContext(Dispatchers.Default) {

            delay(2000)
            1
        }

        val result2 = coroutineScope {

            delay(1000)
            2
        }

        val result = result1 + result2
        println(result)
    }

    Thread.sleep(5000)
}

/**
 * withContext 不会创建新的协程，withContext 允许更改协程的执行线程，withContext 在使用时需要传递一个 CoroutineContext 。
 */
private fun useWithContext() {
    GlobalScope.launch {

        val result1 = withContext(Dispatchers.Default) {
            delay(2000)
            1
        }

        val result2 = withContext(Dispatchers.IO) {

            delay(1000)
            2
        }

        val result = result1 + result2
        println(result)

    }

    Thread.sleep(5000)

}

/**
 * yield
 */
private fun useYield() {

    runBlocking {

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

        job1.join()
        //job2.join()


    }
}


/**
 * delay() 是最常见的挂起函数，类似于线程的 sleep() 函数。但 delay() 并不会阻塞线程。
 */
private fun useDelay() {
    runBlocking {

        println("1: current thread is ${Thread.currentThread().name}")

        launch {
            println("3: current thread is ${Thread.currentThread().name}")

            delay(1000L)

            println("4: current thread is ${Thread.currentThread().name}")
        }
        println("2: current thread is ${Thread.currentThread().name}")

        Thread.sleep(2000L)

        println("5: current thread is ${Thread.currentThread().name}")
    }
}

