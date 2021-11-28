package learncoroutine


import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *
 */
fun main() {

    GlobalScope.launch {
        println("Hello world! GlobalScope current Thread ${Thread.currentThread().name}")
        //第一个挂起点
        delay(100)
        println("Hello world! GlobalScope first after delay current Thread ${Thread.currentThread().name}")
        //第二个挂起点
        delay(200)
        println("Hello world! GlobalScope second after delay current Thread ${Thread.currentThread().name}")
    }
    Thread.sleep(2000)
    println("Hello world! current Thread  ${Thread.currentThread().name}")
}


