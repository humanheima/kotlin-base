package learncoroutine.docs

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by dumingwei on 2020/8/3.
 *
 * Desc:
 */


/*fun main() {
    GlobalScope.launch {
        println(Thread.currentThread().name)
        delay(1000L)
        println("World!")

    }
    println(Thread.currentThread().name)
    println("Hello,")
    Thread.sleep(2000L)
}*/

suspend fun main() {
    GlobalScope.launch {
        println(coroutineContext.get(Job))
    }

}

