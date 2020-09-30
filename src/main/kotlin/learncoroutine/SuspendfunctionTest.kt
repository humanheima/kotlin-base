package learncoroutine

import kotlinx.coroutines.*

/**
 * Created by dumingwei on 2020/9/30.
 *
 * Desc:
 */

fun main() = runBlocking<Unit> {

    GlobalScope.launch(Dispatchers.Main) {
        val result = getString()
        println(result)
    }

}

suspend fun getString(): String {

    return withContext(Dispatchers.IO) {
        "empty String"
    }
}