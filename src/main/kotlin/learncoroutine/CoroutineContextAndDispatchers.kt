package learncoroutine

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

/**
 * 协程上下文和调度器
 */
fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

private fun main1() = runBlocking<Unit> {
    //sampleStart
    launch {
        // context of the parent, main runBlocking coroutine
        println("main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) {
        // not confined -- will work with main thread
        println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) {
        // will get dispatched to DefaultDispatcher
        println("Default               : I'm working in thread ${Thread.currentThread().name}")
    }
    launch(newSingleThreadContext("MyOwnThread")) {
        // will get its own new thread
        println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
    }
}

private fun main2() = runBlocking<Unit> {
    launch(Dispatchers.Unconfined) {
        // 非受限调度器--将和主线程一起工作
        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
        delay(500)
        //注意挂起恢复的时候不是在主线程了
        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
    }
    launch {
        // 父协程的上下文，主 runBlocking 协程
        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        delay(1000)
        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
    }
}

/**
 * 调试协程
 * 在vm option 里面加入
 * -Dkotlinx.coroutines.debug
 */
private fun main3() = runBlocking<Unit> {
    val a = async {
        log("I'm computing a piece of the answer")
        6
    }
    val b = async {
        log("I'm computing another piece of the answer")
        7
    }
    log("The answer is ${a.await() * b.await()}")
}

/**
 * 在线程之间跳转
 * 使用 -Dkotlinx.coroutines.debug JVM 参数运行下面的代码
 */
private fun main4() {
    newSingleThreadContext("Ctx1").use { ctx1 ->
        newSingleThreadContext("Ctx2").use { ctx2 ->
            runBlocking(ctx1) {
                log("Started in ctx1")
                withContext(ctx2) {
                    log("Working in ctx2")
                }
                log("Back to ctx1")
            }
        }
    }
}

/**
 * 上下文中的任务
 */
private fun main5() = runBlocking<Unit> {
    println("My job is ${coroutineContext[Job]}")
}

/**
 * 子协程
 */
private fun main6() = runBlocking {
    // launch a coroutine to process some kind of incoming request
    val request = launch {
        // it spawns two other jobs, one with GlobalScope
        //这个协程是一个独立的协程，不受外部协程的影响
        GlobalScope.launch {
            println("job1: I run in GlobalScope and execute independently!")
            delay(1000)
            println("job1: I am not affected by cancellation of the request")
        }
        // and the other inherits the parent context
        launch {
            delay(100)
            println("job2: I am a child of the request coroutine")
            delay(1000)
            println("job2: I will not execute this line if my parent request is cancelled")
        }
    }
    delay(500)
    request.cancel() // cancel processing of the request
    delay(1000) // delay a second to see what happens
    println("main: Who has survived request cancellation?")
}

/**
 * 父协程的责任
 * 一个父协程总是等待所有的子协程结束。父协程不必显式的跟踪所有启动的子协程。
 * 也不必在最后调用Job.join()等待所有的子协程结束。（这句话不理解，不需要调用Job.join吗，不需要，默认就是等待所有的子协程执行完毕）
 */
private fun main7() = runBlocking<Unit> {
    // launch a coroutine to process some kind of incoming request
    val request = launch {
        repeat(3) { i ->
            // launch a few children jobs
            launch {
                delay((i + 1) * 200L) // variable delay 200ms, 400ms, 600ms
                println("Coroutine $i is done")
            }
        }
        println("request: I'm done and I don't explicitly join my children that are still active")
    }
    request.join() // wait for completion of the request, including all its children
    println("Now processing of the request is complete")
}

/**
 * 命名协程以用于调试
 */
private fun main8() = runBlocking(CoroutineName("main")) {
    log("Started main coroutine")
    // run two background value computations
    val v1 = async(CoroutineName("v1coroutine")) {
        delay(500)
        log("Computing v1")
        252
    }
    val v2 = async(CoroutineName("v2coroutine")) {
        delay(1000)
        log("Computing v2")
        6
    }
    log("The answer for v1 / v2 = ${v1.await() / v2.await()}")
}

/**
 * 组合上下文元素
 * 同时指定调度器和名称
 */
private fun main9() = runBlocking<Unit> {
    launch(Dispatchers.Default + CoroutineName("test")) {
        println("I'm working in thread ${Thread.currentThread().name}")
    }
}

/**
 * 线程本地变量
 */
val threadLocal = ThreadLocal<String?>() // declare thread-local variable

/**
 *
 */
fun mainxxxxx() = runBlocking<Unit> {
    threadLocal.set("main")
    println("Pre-main, current thread: ${Thread.currentThread().name}, thread local value: '${threadLocal.get()}'")
    val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
        println("Launch start, current thread: ${Thread.currentThread().name}, thread local value: '${threadLocal.get()}'")
        yield()

        println("After yield, current thread: ${Thread.currentThread().name}, thread local value: '${threadLocal.get()}'")
    }
    job.join()
    println("Post-main, current thread: ${Thread.currentThread().name}, thread local value: '${threadLocal.get()}'")
}


suspend fun main111() {

    val mDispatcher = Executors.newSingleThreadExecutor { r -> Thread(r, "MyThread") }.asCoroutineDispatcher()

    GlobalScope.launch(mDispatcher) {
        log(1)
    }.join()
    log(2)

}

suspend fun mainMyInterceptor() {

    GlobalScope.launch(MyContinuationInterceptor()) {
        log(1)
        val job = async {
            log(2)
            delay(1000)
            log(3)
            "Hello"
        }
        log(4)
        val result = job.await()
        log("5. $result")
    }.join()
    log(6)

}


class MyContinuationInterceptor : ContinuationInterceptor {

    override val key: CoroutineContext.Key<*>
        get() = ContinuationInterceptor

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        return MyContinuation(continuation)
    }
}

class MyContinuation<T>(val continuation: Continuation<T>) : Continuation<T> {

    override val context: CoroutineContext
        get() = continuation.context

    override fun resumeWith(result: Result<T>) {
        log("<MyContinuation> $result")
        continuation.resumeWith(result)
    }
}