package handbook.twenty_five

import kotlinx.coroutines.*
import kotlin.concurrent.thread

/**
 * Created by dmw on 2019/1/9.
 * Desc: Kotlin的协程及其使用
 * 协程的定义：
 * 协程(coroutine)是一种用户态的轻量级线程，协程的调度完全由用户控制。协程拥有自己的寄存器上下文和栈。协程调度切换时，
 * 将寄存器上下文和栈保存到其他地方，在切回来的时候，恢复先前保存的寄存器上下文和栈，直接操作栈则基本没有内核切换的开销，
 * 可以不加锁的访问全局变量，所以上下文的切换非常快。
 *
 * 在操作系统中，我们知道进程和线程的概念以及区别。而协程(coroutine)相比于线程更加轻量级，协程又称为微线程。
 * 协程虽然是微线程，但是并不会和某一个特定的线程绑定，它可以在 A 线程中执行，并经过某一个时刻的挂起(suspend)，
 * 等下次调度到恢复执行的时候，很可能会在 B 线程中执行。
 *
 * 线程和协程的一个显著区别是，线程的阻塞代价是昂贵的，而协程使用了更简单、代价更小的挂起(suspend)来代替阻塞。
 *
 * 为何要有协程
 * 但是 Kotlin 的协程大大简化了异步编程，使用“同步”的方式来实现异步编程。
 *
 */

fun main(args: Array<String>) {
    //testJobs()
    //testThread()

    //useLaunch()
    //useAsync()
    useRunBlocking()

}

private fun useRunBlocking() {

    runBlocking {
        launch {
            delay(1000)
            println("Hello World!")
        }
        delay(2000)
    }

}

private fun useAsync() {
    GlobalScope.launch {

        val result1 = async {
            delay(2000)
            1
        }
        val result2 = async(start = CoroutineStart.LAZY) {
            delay(2000)
            2
        }

        val result = result1.await() + result2.await()
        println(result)
    }
    Thread.sleep(5000)
}

private fun useLaunch() {

    val job = GlobalScope.launch {
        delay(1000)
        println("Hello Coroutines!")
    }
    Thread.sleep(2000)
}

private fun testJobs() {
    val start = System.currentTimeMillis()

    runBlocking {
        val jobs: List<Job> = List(100000) {
            launch(Dispatchers.Default) {
                // 挂起当前上下文而非阻塞1000ms
                delay(1000)
                println("thread name=" + Thread.currentThread().name)
            }
        }
        jobs.forEach {
            it.join()
        }

    }

    val spend = (System.currentTimeMillis() - start) / 1000

    println("Coroutines: spend= $spend s")
}

private fun testThread() {
    val start = System.currentTimeMillis()

    val threads = List(100000) {
        // 创建新的线程
        thread {
            Thread.sleep(1000)
            println(Thread.currentThread().name)
        }
    }

    threads.forEach { it.join() }

    val spend = (System.currentTimeMillis() - start) / 1000

    println("Threads: spend= $spend s")
}
