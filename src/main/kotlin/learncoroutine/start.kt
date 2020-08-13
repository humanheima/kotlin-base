package learncoroutine

import kotlinx.coroutines.*

/**
 * Created by dumingwei on 2020/8/4.
 *
 * Desc:协程启动篇
 * https://www.bennyhuo.com/2019/04/08/coroutines-start-mode/#more
 */

/**
 * 这段程序采用默认的启动模式，由于我们也没有指定调度器，因此调度器也是默认的。
 * 在 JVM 上，默认调度器的实现与其他语言的实现类似，它在后台专门会有一些线程处理异步任务。
 */
suspend fun mainStartDefault() {

    log(1)
    val job = GlobalScope.launch {
        log(2)
    }
    log(3)
    job.join()
    log(4)
}

suspend fun mainStartUnDispatched() {
    log(1)
    val job = GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
        log(2)
        delay(100)
        log(3)
    }
    log(4)
    job.join()
    log(5)
}

suspend fun mainStartCancel() {
    log(1)
    val job = GlobalScope.launch(start = CoroutineStart.ATOMIC) {
        log(2)
        delay(1000)
        log(3)
    }
    job.cancel()//协程的 cancel 某种意义上更像线程的 interrupt。
    log(4)
    job.join()
}

suspend fun mainStartLazy() {
    log(1)
    val job = GlobalScope.launch(Dispatchers.Main) {
        log(2)
    }
    log(3)
    job.start()
    //job.join() //如果需要保证2在4前面输出，可以使用join方法。
    log(4)
}

fun log(any: Any) {
    println("[" + Thread.currentThread().name + "]" + any.toString())
}