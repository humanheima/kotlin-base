package learncoroutine


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 */

fun main() {
    GlobalScope.launch {
        println("开始执行")
        delay(1000)
        println("延迟1000毫秒后继续执行")
        delay(2000)
        println("延迟2000毫秒后继续执行")
    }
}


