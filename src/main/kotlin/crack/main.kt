package crack

import kotlinx.coroutines.*
import learncoroutine.log

val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    log("${coroutineContext[CoroutineName]} $throwable")
}

suspend fun main() {
    log(1)
    try {
        supervisorScope { //①
            log(2)
            launch(exceptionHandler + CoroutineName("②")) { // ②
                log(3)
                launch(exceptionHandler + CoroutineName("②")) { // ③
                    log(4)
                    delay(100)
                    throw ArithmeticException("Hey!!")
                }
                log(5)
            }
            log(6)
            val job = launch { // ④
                log(7)
                delay(1000)
            }
            try {
                log(8)
                job.join()
                log("9")
            } catch (e: Exception) {
                log("10. $e")
            }
        }
        log(11)
    } catch (e: Exception) {
        log("12. $e")
    }
    log(13)
}

suspend fun mainCoroutineScope() {
    log(1)
    try {
        coroutineScope { //①
            log(2)
            launch { // ②
                log(3)
                launch { // ③
                    log(4)
                    delay(100)
                    throw ArithmeticException("Hey!!")
                }
                log(5)
            }
            log(6)
            val job = launch { // ④
                log(7)
                delay(1000)
            }
            try {
                log(8)
                job.join()
                log("9")
            } catch (e: Exception) {
                log("10. $e")
            }
        }
        log(11)
    } catch (e: Exception) {
        log("12. $e")
    }
    log(13)
}