package crack

import kotlinx.coroutines.*
import learncoroutine.log

/**
 * Created by dumingwei on 2020/8/5.
 *
 * Desc:
 */

suspend fun main() {
    val job = GlobalScope.launch {
        delay(100)
        log(3)
    }

    job.join()

}
