package learncoroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * 并发同步问题
 */
suspend fun CoroutineScope.massiveRun(action: suspend () -> Unit) {
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
}

//加上注解也无法保证原子性,可以考虑使用AtomicInteger来解决
@Volatile
var counter = 0

fun main() = runBlocking<Unit> {
    //sampleStart
    GlobalScope.massiveRun {
        counter++
    }
    println("Counter = $counter")
//sampleEnd
}