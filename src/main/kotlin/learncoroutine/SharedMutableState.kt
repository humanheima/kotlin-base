package learncoroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

/**
 * 并发同步问题
 */
/*suspend fun CoroutineScope.massiveRun(action: suspend () -> Unit) {
    val n = 100  // number of coroutines to launch
    val k = 1000 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        val jobs = List(n) {
            launch {
                repeat(k) { action() }
            }
        }
        jobs.forEach { it.join() }
    }
    println("Completed ${n * k} actions in $time ms")
}*/

suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100  // number of coroutines to launch
    val k = 1000 // times an action is repeated by each coroutine
    val time = measureTimeMillis {
        coroutineScope {
            repeat(n) {
                launch {
                    repeat(k) {
                        action()
                    }
                }
            }
        }
    }
    println("Completed ${n * k} actions in $time ms")
}

//加上注解也无法保证原子性,可以考虑使用AtomicInteger来解决
@Volatile
var counter = 0

private fun main0() = runBlocking<Unit> {
//    GlobalScope.massiveRun {
//        counter++
//    }
    withContext(Dispatchers.Default) {
        massiveRun {
            //注意，这行代码不是原子操作
            counter++
        }
    }
    println("Counter = $counter")
}


val atomicCount = AtomicInteger()

private fun main1() = runBlocking<Unit> {

    withContext(Dispatchers.Default) {
        massiveRun {
            //注释1处
            atomicCount.getAndIncrement()
        }
    }
    println("Counter = ${atomicCount.get()}")
}


/**
 * 使用单线程
 */
val counterContext = newSingleThreadContext("CounterContext")

private fun main2() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            //注释1处，将所有的增加操作限制在单线程上下文中
            withContext(counterContext) {
                counter++
            }
        }
    }
    println("Counter = $counter")
}

private fun main3() = runBlocking {
    //注释1处，将所有的操作都限制在单线程上下文中
    withContext(counterContext) {
        massiveRun {
            counter++
        }
    }
    println("Counter = $counter")
}


val mutex = Mutex()

/**
 * 使用锁互斥
 */
private fun main4() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            // protect each increment with lock
            mutex.withLock {
                counter++
            }
        }
    }
    println("Counter = $counter")
}

// Message types for counterActor
sealed class CounterMsg

object IncCounter : CounterMsg() // one-way message to increment counter
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // a request with reply

// This function launches a new counter actor
fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0 // actor state
    for (msg in channel) { // iterate over incoming messages
        when (msg) {
            is IncCounter -> counter++
            is GetCounter -> msg.response.complete(counter)
        }
    }
}

/*fun main() = runBlocking<Unit> {
    val counter = counterActor() // create the actor
    withContext(Dispatchers.Default) {
        massiveRun {
            counter.send(IncCounter)
        }
    }
    // send a message to get a counter value from an actor
    val response = CompletableDeferred<Int>()
    counter.send(GetCounter(response))
    println("Counter = ${response.await()}")
    counter.close() // shutdown the actor
}*/
